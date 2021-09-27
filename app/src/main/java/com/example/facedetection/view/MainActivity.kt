package com.example.facedetection.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.facedetection.R
import com.example.facedetection.RequestCodeUtil
import com.example.facedetection.api.ImageToUrlRepository
import com.example.facedetection.api.RepositoryCallback
import com.example.facedetection.model.imagetourl.ImageToUrl
import com.example.facedetection.viewmodel.MainActivityViewModel
import java.io.ByteArrayOutputStream
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private lateinit var buttonCamera: Button
    private lateinit var buttonGallery: Button

    private lateinit var imageView: ImageView

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setView()
        setListeners()

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        //viewModel.getFacesInfo(url)

    }

    private fun setView() {
        buttonCamera = findViewById(R.id.main_activity_button_camera)
        buttonGallery = findViewById(R.id.main_activity_button_gallery)

        imageView = findViewById(R.id.main_activity_image)

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

            // just to check:
            imageView.setImageURI(data?.data)

            // essential part of the code:

            if (data != null) {
                val uriImage: Uri = data.data!!
                val imageStream: InputStream = contentResolver.openInputStream(uriImage)!!
                val bitmap: Bitmap = BitmapFactory.decodeStream(imageStream)

                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val output = outputStream.toByteArray()
                val finalImage = Base64.encodeToString(output, Base64.DEFAULT)

                test(finalImage)
            }


        } else if (requestCode == RequestCodeUtil.REQUEST_CODE_CAMERA_IMAGE && resultCode == RESULT_OK) {

            // just to check:
            val imageFromCamera = data?.getParcelableExtra<Bitmap>("data")
            imageView.setImageBitmap(imageFromCamera)

            // essential part of the code:

            if (imageFromCamera != null) {
                val outputStream = ByteArrayOutputStream()
                imageFromCamera.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val output = outputStream.toByteArray()
                val finalImage = Base64.encodeToString(output, Base64.DEFAULT)

                test(finalImage)
            }

        }
    }


    private fun test(image: String) {
        ImageToUrlRepository.getImageUrl(image, object : RepositoryCallback<ImageToUrl> {
            override fun onSuccess(data: ImageToUrl) {}
            override fun onError() {}
        })
    }

}