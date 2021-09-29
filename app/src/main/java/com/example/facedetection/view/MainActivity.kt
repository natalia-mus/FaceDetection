package com.example.facedetection.view

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.facedetection.R
import com.example.facedetection.Status
import com.example.facedetection.model.datamodel.facesinfo.FacesInfo
import com.example.facedetection.util.ConstValues
import com.example.facedetection.util.Permissions
import com.example.facedetection.util.RequestCodeUtil
import com.example.facedetection.viewmodel.MainActivityViewModel
import java.io.ByteArrayOutputStream
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private lateinit var buttonCamera: Button
    private lateinit var buttonGallery: Button
    private lateinit var loadingSection: ConstraintLayout

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setView()
        setObservers()
        setListeners()
    }

    private fun setView() {
        buttonCamera = findViewById(R.id.main_activity_button_camera)
        buttonGallery = findViewById(R.id.main_activity_button_gallery)
        loadingSection = findViewById(R.id.main_activity_loading_section)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }

    private fun setListeners() {
        buttonCamera.setOnClickListener() {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCamera()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CAMERA),
                    Permissions.CAMERA
                )
            }
        }

        buttonGallery.setOnClickListener() {
            openGallery()
        }
    }

    private fun setObservers() {
        viewModel.loading.observe(this, { loadingStatusChanged(it) })
        viewModel.status.observe(this, { statusChanged(it) })
        viewModel.facesInfo.observe(this, { facesInfoChanged(it) })
    }

    private fun loadingStatusChanged(loading: Boolean) {
        if (loading) {
            loadingSection.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.INVISIBLE
        }
    }

    private fun statusChanged(status: Status) {
        if (status == Status.ERROR) {
            Toast.makeText(this, getString(R.string.error_something_went_wrong), Toast.LENGTH_LONG)
                .show()
        }
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
        startActivityForResult(intent, RequestCodeUtil.REQUEST_CODE_CAMERA_IMAGE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ConstValues.INTENT_TYPE_IMAGE
        startActivityForResult(intent, RequestCodeUtil.REQUEST_CODE_GALLERY_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCodeUtil.REQUEST_CODE_GALLERY_IMAGE && resultCode == RESULT_OK) {

            if (data != null && data.data != null) {
                val uriImage: Uri = data.data!!
                val imageStream: InputStream = contentResolver.openInputStream(uriImage)!!
                val bitmap: Bitmap = BitmapFactory.decodeStream(imageStream)

                val finalImage = convertToBase64(bitmap)
                viewModel.detectFaces(finalImage)
            }


        } else if (requestCode == RequestCodeUtil.REQUEST_CODE_CAMERA_IMAGE && resultCode == RESULT_OK) {
            val imageFromCamera = data?.getParcelableExtra<Bitmap>(ConstValues.DATA)

            if (imageFromCamera != null) {
                val finalImage = convertToBase64(imageFromCamera)
                viewModel.detectFaces(finalImage)
            }
        }

    }

    private fun convertToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val output = outputStream.toByteArray()
        val image = Base64.encodeToString(output, Base64.DEFAULT)

        return image
    }

}