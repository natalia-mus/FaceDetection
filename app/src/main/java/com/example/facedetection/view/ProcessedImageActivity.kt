package com.example.facedetection.view

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.facedetection.ImageProcessingOption
import com.example.facedetection.R
import com.example.facedetection.model.datamodel.facesinfo.FacesInfo
import com.example.facedetection.model.datamodel.facesinfo.Photo
import com.example.facedetection.util.ConstValues
import com.example.facedetection.util.ImageConverter
import com.example.facedetection.viewmodel.ProcessedImageViewModel

class ProcessedImageActivity : AppCompatActivity() {

    private lateinit var image: ImageView
    private lateinit var people: TextView
    private lateinit var adults: TextView
    private lateinit var children: TextView
    private lateinit var progressBar: LinearLayout
    private lateinit var optionFaceDetection: LinearLayout
    private lateinit var optionAgeEstimation: LinearLayout
    private lateinit var optionGender: LinearLayout
    private lateinit var optionPixelization: LinearLayout
    private lateinit var optionGrayscale: LinearLayout

    private var faceDetection = false
    private var ageEstimation = false
    private var gender = false
    private var pixelization = false
    private var grayscale = false

    private lateinit var viewModel: ProcessedImageViewModel

    private lateinit var bitmap: Bitmap
    private lateinit var processedImage: Bitmap
    private lateinit var photoData: Photo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_processed_image)

        setView()
        setObservers()
        setOptions()
    }

    private fun selectOption(option: ImageProcessingOption, selected: Boolean) {
        progressBar.visibility = View.VISIBLE
        faceDetection = false
        ageEstimation = false
        gender = false
        pixelization = false
        grayscale = false
        optionFaceDetection.isSelected = false
        optionAgeEstimation.isSelected = false
        optionGender.isSelected = false
        optionPixelization.isSelected = false
        optionGrayscale.isSelected = false

        if (selected) {
            imageChanged(bitmap)
        } else {
            when (option) {
                ImageProcessingOption.FACE_DETECTION -> {
                    faceDetection = true
                    optionFaceDetection.isSelected = true
                }
                ImageProcessingOption.AGE_ESTIMATION -> {
                    ageEstimation = true
                    optionAgeEstimation.isSelected = true
                }
                ImageProcessingOption.GENDER -> {
                    gender = true
                    optionGender.isSelected = true
                }
                ImageProcessingOption.PIXELIZATION -> {
                    pixelization = true
                    optionPixelization.isSelected = true
                }
                ImageProcessingOption.GRAYSCALE -> {
                    grayscale = true
                    optionGrayscale.isSelected = true
                }
            }
        }
    }

    private fun setView() {
        image = findViewById(R.id.processed_image_activity_image)
        people = findViewById(R.id.processed_image_activity_people)
        adults = findViewById(R.id.processed_image_activity_adults)
        children = findViewById(R.id.processed_image_activity_children)
        progressBar = findViewById(R.id.activity_processed_image_progress_bar)
        optionFaceDetection = findViewById(R.id.option_face_detection)
        optionAgeEstimation = findViewById(R.id.option_age_estimation)
        optionGender = findViewById(R.id.option_gender)
        optionPixelization = findViewById(R.id.option_pixelization)
        optionGrayscale = findViewById(R.id.option_grayscale)

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
                photoData = facesInfo.photos[0]
                viewModel.processImage(photoData)
            }
        }

    }

    private fun setObservers() {
        viewModel.peopleCount.observe(this) { peopleCountChanged(it) }
        viewModel.childrenCount.observe(this) { childrenCountChanged(it) }
        viewModel.adultsCount.observe(this) { adultsCountChanged(it) }
        viewModel.processedImage.observe(this) {
            processedImage = it
            imageChanged(bitmap)
        }
        viewModel.pixelatedImage.observe(this) { imageChanged(it) }
    }

    private fun setOptions() {
        val genderInfoAvailable = viewModel.isGenderInfoAvailable(photoData)

        optionFaceDetection.setOnClickListener() {
            if (!faceDetection) {
                imageChanged(processedImage)
            }
            selectOption(ImageProcessingOption.FACE_DETECTION, faceDetection)
        }

        optionAgeEstimation.setOnClickListener() {
            if (!ageEstimation) {
                imageChanged(viewModel.estimateAge(photoData))
            }
            selectOption(ImageProcessingOption.AGE_ESTIMATION, ageEstimation)
        }

        if (genderInfoAvailable) {
            optionGender.setOnClickListener() {
                if (!gender) {
                    imageChanged(viewModel.getGender(photoData, resources))
                }
                selectOption(ImageProcessingOption.GENDER, gender)
            }
        } else {
            optionGender.visibility = View.GONE
        }

        optionPixelization.setOnClickListener() {
            if (!pixelization) {
                viewModel.pixelateImage(bitmap)
            }
            selectOption(ImageProcessingOption.PIXELIZATION, pixelization)
        }

        optionGrayscale.setOnClickListener() {
            if (!grayscale) {
                imageChanged(viewModel.grayscaleImage(bitmap))
            }
            selectOption(ImageProcessingOption.GRAYSCALE, grayscale)
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