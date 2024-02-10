package com.example.facedetection.view

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.facedetection.R
import com.example.facedetection.Settings
import com.example.facedetection.Status
import com.example.facedetection.model.datamodel.apiusage.APIUsageData
import com.example.facedetection.model.datamodel.facesinfo.FacesInfo
import com.example.facedetection.util.ConstValues
import com.example.facedetection.util.Permissions
import com.example.facedetection.util.RequestCodeUtil
import com.example.facedetection.viewmodel.MainActivityViewModel
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private lateinit var buttonCamera: LinearLayout
    private lateinit var buttonGallery: LinearLayout
    private lateinit var loadingSection: ConstraintLayout

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolbar()
        setView()

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        setObservers()
        setListeners()
        initSettings()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.getItem(0)?.isEnabled = loadingSection.visibility != View.VISIBLE                     // preventing not to call API usage option while photo is being processed
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_api_usage -> viewModel.checkAPIUsage()
            R.id.menu_item_change_api_key -> changeAPIKey()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isAPIKeySet() = Settings.isAPIKeySet()

    private fun onCameraButtonClick() {
        if (isAPIKeySet()) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera()

            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), Permissions.CAMERA)
            }
        } else {
            changeAPIKey()
        }
    }

    private fun onGalleryButtonClick() {
        if (isAPIKeySet()) {
            openGallery()
        } else {
            changeAPIKey()
        }
    }

    private fun setToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun setView() {
        buttonCamera = findViewById(R.id.main_activity_button_camera)
        buttonGallery = findViewById(R.id.main_activity_button_gallery)
        loadingSection = findViewById(R.id.main_activity_loading_section)
    }

    private fun setListeners() {
        buttonCamera.setOnClickListener() {
            onCameraButtonClick()
        }

        buttonGallery.setOnClickListener() {
            onGalleryButtonClick()
        }
    }

    private fun setObservers() {
        viewModel.loading.observe(this) { loadingStatusChanged(it) }
        viewModel.status.observe(this) { statusChanged(it) }
        viewModel.apiUsage.observe(this) { apiUsageChanged(it) }
        viewModel.facesInfo.observe(this) { facesInfoChanged(it) }
    }

    private fun initSettings() {
        Settings.initSettings(applicationContext)
    }

    private fun loadingStatusChanged(loading: Boolean) {
        if (loading) {
            loadingSection.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.GONE
        }
    }

    private fun statusChanged(status: Status) {
        when (status) {
            Status.ERROR -> Toast.makeText(this, getString(R.string.error_something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }

    private fun apiUsageChanged(apiUsage: APIUsageData) {
        val info = APIUsageInfo(this, apiUsage)
        info.show()
    }

    private fun facesInfoChanged(facesInfo: FacesInfo) {
        val intent = Intent(this, ProcessedImageActivity::class.java)
        intent.putExtra(ConstValues.FACES_INFO, facesInfo)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Permissions.CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) openCamera()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // documentation still gives this way as correct:
        startActivityForResult(intent, RequestCodeUtil.REQUEST_CODE_CAMERA_IMAGE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ConstValues.INTENT_TYPE_IMAGE

        // documentation still gives this way as correct:
        startActivityForResult(intent, RequestCodeUtil.REQUEST_CODE_GALLERY_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // documentation still gives this way as correct:
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCodeUtil.REQUEST_CODE_GALLERY_IMAGE && resultCode == RESULT_OK) {

            if (data != null && data.data != null) {
                val uriImage: Uri = data.data!!
                val imageStream: InputStream = contentResolver.openInputStream(uriImage)!!
                val photoBitmap: Bitmap = BitmapFactory.decodeStream(imageStream)

                viewModel.setImage(photoBitmap)
                viewModel.detectFaces(photoBitmap)
            }

        } else if (requestCode == RequestCodeUtil.REQUEST_CODE_CAMERA_IMAGE && resultCode == RESULT_OK) {
            val imageFromCamera = data?.getParcelableExtra<Bitmap>(ConstValues.DATA)

            if (imageFromCamera != null) {
                viewModel.setImage(imageFromCamera)
                viewModel.detectFaces(imageFromCamera)
            }
        }

    }

    private fun changeAPIKey() {
        val intent = Intent(this, ChangeAPIKeyActivity::class.java)
        startActivity(intent)
    }

}