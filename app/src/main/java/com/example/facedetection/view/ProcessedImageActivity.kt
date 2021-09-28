package com.example.facedetection.view

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.facedetection.R
import com.example.facedetection.model.datamodel.facesinfo.FacesInfo
import com.example.facedetection.util.ConstValues
import com.example.facedetection.viewmodel.ProcessedImageViewModel

class ProcessedImageActivity : AppCompatActivity() {

    private lateinit var photo: ImageView
    private lateinit var faces: ImageView
    private lateinit var people: TextView
    private lateinit var adults: TextView
    private lateinit var children: TextView

    private lateinit var viewModel: ProcessedImageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_processed_image)

        setView()
        setObservers()
    }

    private fun setView() {
        photo = findViewById(R.id.processed_image_activity_photo)
        faces = findViewById(R.id.processed_image_activity_faces)
        people = findViewById(R.id.processed_image_activity_people)
        adults = findViewById(R.id.processed_image_activity_adults)
        children = findViewById(R.id.processed_image_activity_children)

        viewModel = ViewModelProvider(this).get(ProcessedImageViewModel::class.java)


        if (intent.hasExtra(ConstValues.FACES_INFO)) {
            val facesInfo = intent.getParcelableExtra<FacesInfo>(ConstValues.FACES_INFO)

            if (facesInfo != null) {
                val imageUrl = facesInfo.photos[0].url

                Glide.with(this).load(imageUrl).into(photo)

                val photo = facesInfo.photos[0]
                viewModel.processImage(photo)
            }
        }
    }

    private fun setObservers() {
        viewModel.peopleCount.observe(this, { peopleCountChanged(it) })
        viewModel.childrenCount.observe(this, { childrenCountChanged(it) })
        viewModel.adultsCount.observe(this, { adultsCountChanged(it) })
        viewModel.processedImage.observe(this, { processedImageChanged(it) })
    }


    private fun peopleCountChanged(peopleCount: Int) {
        people.text = peopleCount.toString()
    }

    private fun childrenCountChanged(childrenCount: Int) {
        children.text = childrenCount.toString()
    }

    private fun adultsCountChanged(adultsCount: Int) {
        adults.text = adultsCount.toString()
    }

    private fun processedImageChanged(processedImage: Bitmap) {
        faces.setImageBitmap(processedImage)
    }

}