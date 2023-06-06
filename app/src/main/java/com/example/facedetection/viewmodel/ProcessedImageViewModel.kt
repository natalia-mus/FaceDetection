package com.example.facedetection.viewmodel

import android.content.res.Resources
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.facedetection.model.ImageBitmapProcessor
import com.example.facedetection.model.ImageDataProcessor
import com.example.facedetection.model.datamodel.facesinfo.Photo

class ProcessedImageViewModel() : ViewModel() {

    val peopleCount = MutableLiveData<Int>()
    val adultsCount = MutableLiveData<Int>()
    val childrenCount = MutableLiveData<Int>()
    val processedImage = MutableLiveData<Bitmap>()
    val pixelatedImage = MutableLiveData<Bitmap>()

    private val imageBitmapProcessor: ImageBitmapProcessor by lazy { ImageBitmapProcessor() }
    private lateinit var imageDataProcessor: ImageDataProcessor


    fun isGenderInfoAvailable(photo: Photo): Boolean {
        if (!this::imageDataProcessor.isInitialized) {
            imageDataProcessor = ImageDataProcessor(photo)
        }

        return imageDataProcessor.isGenderInfoAvailable()
    }

    fun processImage(photo: Photo) {
        if (!this::imageDataProcessor.isInitialized) {
            imageDataProcessor = ImageDataProcessor(photo)
        }

        peopleCount.value = imageDataProcessor.countPeople()
        adultsCount.value = imageDataProcessor.countAdults()
        childrenCount.value = imageDataProcessor.countChildren()
        processedImage.value = imageDataProcessor.detectFaces()
    }

    fun estimateAge(photo: Photo): Bitmap {
        if (!this::imageDataProcessor.isInitialized) {
            imageDataProcessor = ImageDataProcessor(photo)
        }

        return imageDataProcessor.estimateAge()
    }

    fun getGender(photo: Photo, resources: Resources): Bitmap {
        if (!this::imageDataProcessor.isInitialized) {
            imageDataProcessor = ImageDataProcessor(photo)
        }

        return imageDataProcessor.getGender(resources)
    }

    fun pixelateImage(bitmap: Bitmap) {
        pixelatedImage.value = imageBitmapProcessor.pixelateImage(bitmap)
    }

    fun grayscaleImage(bitmap: Bitmap): Bitmap {
        return imageBitmapProcessor.grayscaleImage(bitmap)
    }

}