package com.example.facedetection.view

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import com.example.facedetection.Image
import com.example.facedetection.ImageProcessingOption
import com.example.facedetection.R
import com.example.facedetection.model.datamodel.facesinfo.FacesInfo
import com.example.facedetection.model.datamodel.facesinfo.Photo
import com.example.facedetection.util.ConstValues
import com.example.facedetection.util.ImageConverter
import com.example.facedetection.viewmodel.ProcessedImageViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
    private lateinit var saveImageButton: FloatingActionButton

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

    private fun handleImageSavingStatus(imageSavedSuccessfully: Boolean) {
        var message = resources.getString(R.string.image_saved_successfully)

        if (!imageSavedSuccessfully) {
            message = resources.getString(R.string.error_something_went_wrong)
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveImage() {
        viewModel.saveImage(this, image.drawable.toBitmap(image.drawable.intrinsicWidth, image.drawable.intrinsicHeight))
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
        saveImageButton = findViewById(R.id.processed_image_activity_save)

        saveImageButton.setOnClickListener() {
            saveImage()
        }

        viewModel = ViewModelProvider(this).get(ProcessedImageViewModel::class.java)


        val byteArray = Image.getImage()
        if (byteArray != null) {
            bitmap = ImageConverter.convertToBitmap(byteArray)
            imageChanged(bitmap)
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
            imageChanged(processedImage)
        }
        viewModel.pixelatedImage.observe(this) { imageChanged(it) }
        viewModel.imageSavedSuccessfully.observe(this) { handleImageSavingStatus(it) }
    }

    private fun setOptions() {
        val genderInfoAvailable = viewModel.isGenderInfoAvailable(photoData)

        optionFaceDetection.setOnClickListener() {
            if (!faceDetection) {
                viewModel.detectFaces(photoData)
            }
            selectOption(ImageProcessingOption.FACE_DETECTION, faceDetection)
        }

        optionAgeEstimation.setOnClickListener() {
            if (!ageEstimation) {
                viewModel.estimateAge(photoData)
            }
            selectOption(ImageProcessingOption.AGE_ESTIMATION, ageEstimation)
        }

        if (genderInfoAvailable) {
            optionGender.setOnClickListener() {
                if (!gender) {
                    viewModel.getGender(photoData, resources)
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
            }, 300
        )
    }

}