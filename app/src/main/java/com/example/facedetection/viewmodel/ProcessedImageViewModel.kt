package com.example.facedetection.viewmodel

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

    private lateinit var imageProcessor: ImageDataProcessor


    fun processImage(photo: Photo) {
        if (!this::imageProcessor.isInitialized) {
            imageProcessor = ImageDataProcessor(photo)
        }

        peopleCount.value = imageProcessor.countPeople()
        adultsCount.value = imageProcessor.countAdults()
        childrenCount.value = imageProcessor.countChildren()
        processedImage.value = imageProcessor.detectFaces()
    }

    fun estimateAge(photo: Photo): Bitmap {
        if (!this::imageProcessor.isInitialized) {
            imageProcessor = ImageDataProcessor(photo)
        }

        return imageProcessor.estimateAge()
    }

    fun pixelateImage(bitmap: Bitmap) {
        loading.value = true
        pixelatedImage.value = ImageBitmapProcessor.pixelateImage(bitmap)
        loading.value = false
    }

    fun grayscaleImage(bitmap: Bitmap): Bitmap {
        return ImageBitmapProcessor.grayscaleImage(bitmap)
    }

}