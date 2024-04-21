package com.example.facedetection.viewmodel

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.facedetection.ImageProcessingOption
import com.example.facedetection.model.ImageBitmapProcessor
import com.example.facedetection.model.ImageDataProcessor
import com.example.facedetection.model.datamodel.facesinfo.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ProcessedImageViewModel : ViewModel() {

    val peopleCount = MutableLiveData<Int>()
    val adultsCount = MutableLiveData<Int>()
    val childrenCount = MutableLiveData<Int>()
    val processedImage = MutableLiveData<Bitmap>()

    val imageSavedSuccessfully = MutableLiveData<Boolean>()

    private val imageBitmapProcessor: ImageBitmapProcessor by lazy { ImageBitmapProcessor() }
    private lateinit var imageDataProcessor: ImageDataProcessor


    fun applyImageOptions(bitmap: Bitmap, photo: Photo, options: LinkedList<ImageProcessingOption>, resources: Resources) {
        if (options.isEmpty()) {
            processedImage.postValue(bitmap)

        } else {
            var resultBitmap: Bitmap?

            when (options.pollFirst()) {
                ImageProcessingOption.FACE_DETECTION -> {
                    initDataProcessor(photo)

                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            resultBitmap = imageDataProcessor.detectFaces()
                        }

                        if (resultBitmap != null) {
                            applyImageOptions(resultBitmap!!, photo, options, resources)
                        }
                    }
                }

                ImageProcessingOption.AGE_ESTIMATION -> {
                    initDataProcessor(photo)

                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            resultBitmap = imageDataProcessor.estimateAge()
                        }

                        if (resultBitmap != null) {
                            applyImageOptions(resultBitmap!!, photo, options, resources)
                        }
                    }
                }

                ImageProcessingOption.GENDER -> {
                    initDataProcessor(photo)

                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            resultBitmap = imageDataProcessor.getGender(resources)
                        }

                        if (resultBitmap != null) {
                            applyImageOptions(resultBitmap!!, photo, options, resources)
                        }
                    }
                }

                ImageProcessingOption.PIXELIZATION -> {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            resultBitmap = imageBitmapProcessor.pixelateImage(bitmap)
                        }

                        if (resultBitmap != null) {
                            applyImageOptions(resultBitmap!!, photo, options, resources)
                        }
                    }
                }

                ImageProcessingOption.GRAYSCALE -> {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            resultBitmap = imageBitmapProcessor.grayscaleImage(bitmap)
                        }

                        if (resultBitmap != null) {
                            applyImageOptions(resultBitmap!!, photo, options, resources)
                        }
                    }
                }

                ImageProcessingOption.SEPIA -> {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            resultBitmap = imageBitmapProcessor.convertToSepia(bitmap)
                        }

                        if (resultBitmap != null) {
                            applyImageOptions(resultBitmap!!, photo, options, resources)
                        }
                    }
                }
            }
        }
    }

    fun isGenderInfoAvailable(photo: Photo): Boolean {
        initDataProcessor(photo)
        return imageDataProcessor.isGenderInfoAvailable()
    }

    fun processImage(photo: Photo) {
        initDataProcessor(photo)
        peopleCount.value = imageDataProcessor.countPeople()
        adultsCount.value = imageDataProcessor.countAdults()
        childrenCount.value = imageDataProcessor.countChildren()
    }

    fun saveImage(context: Context, bitmap: Bitmap) {
        imageSavedSuccessfully.value = imageBitmapProcessor.saveImage(context, bitmap)
    }

    private fun initDataProcessor(photo: Photo) {
        if (!this::imageDataProcessor.isInitialized) {
            imageDataProcessor = ImageDataProcessor(photo)
        }
    }

}