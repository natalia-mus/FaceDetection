package com.example.facedetection.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.facedetection.R
import com.example.facedetection.model.datamodel.facesinfo.FacesInfo
import com.example.facedetection.util.ConstValues
import com.example.facedetection.util.ImageConverter
import com.example.facedetection.viewmodel.ProcessedImageViewModel

class ProcessedImageActivity : AppCompatActivity() {

    private lateinit var image: ImageView
    private lateinit var people: TextView
    private lateinit var adults: TextView
    private lateinit var children: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var optionFaceDetection: LinearLayout
    private lateinit var optionAgeEstimation: LinearLayout
    private lateinit var optionPixelization: LinearLayout

    private var faceDetection = false
    private var ageEstimation = false
    private var pixelization = false

    private lateinit var viewModel: ProcessedImageViewModel

    private lateinit var bitmap: Bitmap
    private lateinit var processedImage: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_processed_image)

        setView()
        setObservers()
        options()
    }

    private fun setView() {
        image = findViewById(R.id.processed_image_activity_image)
        people = findViewById(R.id.processed_image_activity_people)
        adults = findViewById(R.id.processed_image_activity_adults)
        children = findViewById(R.id.processed_image_activity_children)
        progressBar = findViewById(R.id.activity_processed_image_progress_bar)
        optionFaceDetection = findViewById(R.id.option_face_detection)
        optionAgeEstimation = findViewById(R.id.option_age_estimation)
        optionPixelization = findViewById(R.id.option_pixelization)

        viewModel = ViewModelProvider(this).get(ProcessedImageViewModel::class.java)


        if (intent.hasExtra(ConstValues.BITMAP)) {
            val byteArray = intent.getByteArrayExtra(ConstValues.BITMAP)
            if (byteArray != null) {
                bitmap = ImageConverter.convertToBitmap(byteArray)
            }
        }


        if (intent.hasExtra(ConstValues.FACES_INFO)) {
            val facesInfo = intent.getParcelableExtra<FacesInfo>(ConstValues.FACES_INFO)

            if (facesInfo != null) {
                val processedImageData = facesInfo.photos[0]
                viewModel.processImage(processedImageData)
            }
        }

    }

    private fun setObservers() {
        viewModel.peopleCount.observe(this, { peopleCountChanged(it) })
        viewModel.childrenCount.observe(this, { childrenCountChanged(it) })
        viewModel.adultsCount.observe(this, { adultsCountChanged(it) })
        viewModel.processedImage.observe(this, {
            processedImage = it
            imageChanged(bitmap)
        })
    }

    private fun options() {
        val optionFaceDetectionIcon = findViewById<ImageView>(R.id.option_face_detection_icon)
        val optionFaceDetectionText = findViewById<TextView>(R.id.option_face_detection_text)
        val optionAgeEstimationIcon = findViewById<ImageView>(R.id.option_age_estimation_icon)
        val optionAgeEstimationText = findViewById<TextView>(R.id.option_age_estimation_text)
        val optionPixelizationIcon = findViewById<ImageView>(R.id.option_pixelization_icon)
        val optionPixelizationText = findViewById<TextView>(R.id.option_pixelization_text)

        optionFaceDetection.setOnClickListener() {
            progressBar.visibility = View.VISIBLE

            if (faceDetection) {
                faceDetection = false
                optionFaceDetectionIcon.setColorFilter(resources.getColor(R.color.white, null))
                optionFaceDetectionText.setTextColor(resources.getColor(R.color.white, null))
                imageChanged(bitmap)
            } else {
                faceDetection = true
                ageEstimation = false
                pixelization = false
                optionFaceDetectionIcon.setColorFilter(resources.getColor(R.color.blue_option_on, null))
                optionFaceDetectionText.setTextColor(resources.getColor(R.color.blue_option_on, null))
                optionAgeEstimationIcon.setColorFilter(resources.getColor(R.color.white, null))
                optionAgeEstimationText.setTextColor(resources.getColor(R.color.white, null))
                optionPixelizationIcon.setColorFilter(resources.getColor(R.color.white, null))
                optionPixelizationText.setTextColor(resources.getColor(R.color.white, null))
                imageChanged(processedImage)
            }
        }

        optionAgeEstimation.setOnClickListener() {
            progressBar.visibility = View.VISIBLE

            if (ageEstimation) {
                ageEstimation = false
                optionAgeEstimationIcon.setColorFilter(resources.getColor(R.color.white, null))
                optionAgeEstimationText.setTextColor(resources.getColor(R.color.white, null))
            } else {
                faceDetection = false
                ageEstimation = true
                pixelization = false
                optionFaceDetectionIcon.setColorFilter(resources.getColor(R.color.white, null))
                optionFaceDetectionText.setTextColor(resources.getColor(R.color.white, null))
                optionAgeEstimationIcon.setColorFilter(resources.getColor(R.color.blue_option_on, null))
                optionAgeEstimationText.setTextColor(resources.getColor(R.color.blue_option_on, null))
                optionPixelizationIcon.setColorFilter(resources.getColor(R.color.white, null))
                optionPixelizationText.setTextColor(resources.getColor(R.color.white, null))
            }
        }

        optionPixelization.setOnClickListener() {
            progressBar.visibility = View.VISIBLE

            if (pixelization) {
                pixelization = false
                optionPixelizationIcon.setColorFilter(resources.getColor(R.color.white, null))
                optionPixelizationText.setTextColor(resources.getColor(R.color.white, null))
                imageChanged(bitmap)
            } else {
                faceDetection = false
                ageEstimation = false
                pixelization = true
                optionFaceDetectionIcon.setColorFilter(resources.getColor(R.color.white, null))
                optionFaceDetectionText.setTextColor(resources.getColor(R.color.white, null))
                optionAgeEstimationIcon.setColorFilter(resources.getColor(R.color.white, null))
                optionAgeEstimationText.setTextColor(resources.getColor(R.color.white, null))
                optionPixelizationIcon.setColorFilter(resources.getColor(R.color.blue_option_on, null))
                optionPixelizationText.setTextColor(resources.getColor(R.color.blue_option_on, null))
                imageChanged(viewModel.pixelateImage(bitmap))
            }
        }
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

    private fun imageChanged(bitmap: Bitmap) {
        image.setImageBitmap(bitmap)

        Handler().postDelayed(
            {
                image.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }, 500
        )
    }

}