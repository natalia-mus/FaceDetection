package com.example.facedetection.viewmodel

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.facedetection.Image
import com.example.facedetection.ImageProcessingOption
import com.example.facedetection.model.ImageBitmapProcessor
import com.example.facedetection.model.ImageDataProcessor
import com.example.facedetection.model.datamodel.facesinfo.Photo
import com.example.facedetection.util.ImageConverter
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

    private var bitmap: Bitmap? = null


    init {
        getOriginalBitmap()
        processedImage.value = bitmap!!
    }

    fun applyImageOptions(photo: Photo, options: LinkedList<ImageProcessingOption>, resources: Resources) {
        applyImageOptions(photo, options, resources, true)
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

    private fun applyImageOptions(photo: Photo, options: LinkedList<ImageProcessingOption>, resources: Resources, getOriginalBitmap: Boolean) {
        if (getOriginalBitmap) {
            getOriginalBitmap()
        }

        if (options.isEmpty()) {
            processedImage.postValue(bitmap!!)

        } else {
            when (options.pollFirst()) {
                ImageProcessingOption.FACE_DETECTION -> {
                    initDataProcessor(photo)

                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            bitmap = imageDataProcessor.detectFaces()
                        }

                        applyImageOptions(photo, options, resources, false)
                    }
                }

                ImageProcessingOption.AGE_ESTIMATION -> {
                    initDataProcessor(photo)

                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            bitmap = imageDataProcessor.estimateAge()
                        }

                        applyImageOptions(photo, options, resources, false)
                    }
                }

                ImageProcessingOption.GENDER -> {
                    initDataProcessor(photo)

                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            bitmap = imageDataProcessor.getGender(resources)
                        }

                        applyImageOptions(photo, options, resources, false)
                    }
                }

                ImageProcessingOption.PIXELIZATION -> {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            bitmap = imageBitmapProcessor.pixelateImage(bitmap!!)
                        }

                        applyImageOptions(photo, options, resources, false)
                    }
                }

                ImageProcessingOption.GRAYSCALE -> {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            bitmap = imageBitmapProcessor.grayscaleImage(bitmap!!)
                        }

                        applyImageOptions(photo, options, resources, false)
                    }
                }

                ImageProcessingOption.SEPIA -> {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            bitmap = imageBitmapProcessor.convertToSepia(bitmap!!)
                        }

                        applyImageOptions(photo, options, resources, false)
                    }
                }

                ImageProcessingOption.MIRROR_IMAGE -> {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            bitmap = imageBitmapProcessor.mirrorImage(bitmap!!)
                        }

                        applyImageOptions(photo, options, resources, false)
                    }
                }

                ImageProcessingOption.UPSIDE_DOWN -> {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            bitmap = imageBitmapProcessor.turnImageUpsideDown(bitmap!!)
                        }

                        applyImageOptions(photo, options, resources, false)
                    }
                }
            }
        }
    }

    private fun getOriginalBitmap() {
        val byteArray = Image.getImage()
        if (byteArray != null) {
            bitmap = ImageConverter.convertToBitmap(byteArray)
        }
    }

    private fun initDataProcessor(photo: Photo) {
        if (!this::imageDataProcessor.isInitialized) {
            imageDataProcessor = ImageDataProcessor(photo)
        }
    }

}