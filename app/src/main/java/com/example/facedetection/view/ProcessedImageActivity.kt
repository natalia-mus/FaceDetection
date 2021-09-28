package com.example.facedetection.view

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.facedetection.R
import com.example.facedetection.model.datamodel.facesinfo.FacesInfo
import com.example.facedetection.viewmodel.ProcessedImageViewModel

class ProcessedImageActivity : AppCompatActivity() {

    private lateinit var photo: ImageView
    private lateinit var image: ImageView

    private lateinit var viewModel: ProcessedImageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_processed_image)

        setView()
        setObservers()

        if (intent.hasExtra("faces_info")) {
            val facesInfo = intent.getParcelableExtra<FacesInfo>("faces_info")
            val imageUrl =
                facesInfo!!.photos[0].url                                            // !!
            Glide.with(this).load(imageUrl).into(photo)

            val photo = facesInfo.photos[0]
            viewModel.processImage(photo)
        }

    }

    private fun setView() {
        photo = findViewById(R.id.processed_image_activity_photo)
        image = findViewById(R.id.processed_image_activity_rectangles)

        viewModel = ViewModelProvider(this).get(ProcessedImageViewModel::class.java)
    }

    private fun setObservers() {
        viewModel.peopleCount.observe(this, { peopleCountChanged(it) })
        viewModel.childrenCount.observe(this, { childrenCountChanged(it) })
        viewModel.adultsCount.observe(this, { adultsCountChanged(it) })
        viewModel.processedImage.observe(this, { processedImageChanged(it) })
    }


    private fun peopleCountChanged(peopleCount: Int) {
        Log.e("people", peopleCount.toString())
    }

    private fun childrenCountChanged(childrenCount: Int) {
        Log.e("children", childrenCount.toString())
    }

    private fun adultsCountChanged(adultsCount: Int) {
        Log.e("adults", adultsCount.toString())
    }

    private fun processedImageChanged(processedImage: Bitmap) {
        image.setImageBitmap(processedImage)
    }

}