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

        val url =
            "https://www.elleman.pl/uploads/media/default/0005/40/cec344175ad1976fe2a78e04a2843f97ce77270b.jpeg"

        setView()
        setListeners()

        //val image = "R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7"
        val image =
            "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"

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.getFacesInfo(url)

        // checking if API works:
        ImageToUrlRepository.getImageUrl(image, object : RepositoryCallback<ImageToUrl> {
            override fun onSuccess(data: ImageToUrl) {}
            override fun onError() {}
        })

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

                Log.e("base64", finalImage)
                Log.e("base64", finalImage.length.toString())
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

                Log.e("base64", finalImage)
                Log.e("base64 length", finalImage.length.toString())
            }

        }
    }

}