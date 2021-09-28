package com.example.facedetection.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.example.facedetection.R
import com.example.facedetection.RequestCodeUtil
import com.example.facedetection.model.datamodel.facesinfo.FacesInfo
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
            openCamera()
        }

        buttonGallery.setOnClickListener() {
            openGallery()
        }
    }

    private fun setObservers() {
        viewModel.loading.observe(this, { loadingStatusChanged(it) })
        viewModel.facesInfo.observe(this, { facesInfoChanged(it) })
    }

    private fun loadingStatusChanged(loading: Boolean) {
        if (loading) {
            loadingSection.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.INVISIBLE
        }
    }

    private fun facesInfoChanged(facesInfo: FacesInfo) {
        val intent = Intent(this, ProcessedImageActivity::class.java)
        //intent.putExtra("image_url", facesInfo.photos[0].url)
        intent.putExtra("faces_info", facesInfo)
        startActivity(intent)
    }

    // check permissions first

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, RequestCodeUtil.REQUEST_CODE_CAMERA_IMAGE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, RequestCodeUtil.REQUEST_CODE_GALLERY_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCodeUtil.REQUEST_CODE_GALLERY_IMAGE && resultCode == RESULT_OK) {

            if (data != null) {
                val uriImage: Uri = data.data!!
                val imageStream: InputStream = contentResolver.openInputStream(uriImage)!!
                val bitmap: Bitmap = BitmapFactory.decodeStream(imageStream)

                val finalImage = convertToBase64(bitmap)
                viewModel.detectFaces(finalImage)
            }


        } else if (requestCode == RequestCodeUtil.REQUEST_CODE_CAMERA_IMAGE && resultCode == RESULT_OK) {
            val imageFromCamera = data?.getParcelableExtra<Bitmap>("data")

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