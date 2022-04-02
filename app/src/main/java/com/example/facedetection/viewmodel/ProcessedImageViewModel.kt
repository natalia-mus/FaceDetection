package com.example.facedetection.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.facedetection.model.ImageDataProcessor
import com.example.facedetection.model.datamodel.facesinfo.Photo
import com.example.facedetection.model.ImageBitmapProcessor

class ProcessedImageViewModel() : ViewModel() {

    val loading = MutableLiveData<Boolean>(false)
    val peopleCount = MutableLiveData<Int>()
    val adultsCount = MutableLiveData<Int>()
    val childrenCount = MutableLiveData<Int>()
    val processedImage = MutableLiveData<Bitmap>()
    val pixelatedImage = MutableLiveData<Bitmap>()

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

    fun getGender(photo: Photo, context: Context): Bitmap {
        if (!this::imageDataProcessor.isInitialized) {
            imageDataProcessor = ImageDataProcessor(photo)
        }

        return imageDataProcessor.getGender(context)
    }

    fun pixelateImage(bitmap: Bitmap) {
        pixelatedImage.value = ImageBitmapProcessor.pixelateImage(bitmap)
    }

    fun grayscaleImage(bitmap: Bitmap): Bitmap {
        return ImageBitmapProcessor.grayscaleImage(bitmap)
    }

}