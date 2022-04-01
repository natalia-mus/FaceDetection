package com.example.facedetection.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.facedetection.model.ImageProcessing
import com.example.facedetection.model.datamodel.facesinfo.Photo
import com.example.facedetection.util.ImagePixelizator

class ProcessedImageViewModel() : ViewModel() {

    val loading = MutableLiveData<Boolean>(false)
    val peopleCount = MutableLiveData<Int>()
    val adultsCount = MutableLiveData<Int>()
    val childrenCount = MutableLiveData<Int>()
    val processedImage = MutableLiveData<Bitmap>()
    val pixelatedImage = MutableLiveData<Bitmap>()

    private lateinit var imageProcessor: ImageProcessing


    fun processImage(photo: Photo) {
        if (!this::imageProcessor.isInitialized) {
            imageProcessor = ImageProcessing(photo)
        }

        peopleCount.value = imageProcessor.countPeople()
        adultsCount.value = imageProcessor.countAdults()
        childrenCount.value = imageProcessor.countChildren()
        processedImage.value = imageProcessor.detectFaces()
    }

    fun estimateAge(photo: Photo): Bitmap {
        if (!this::imageProcessor.isInitialized) {
            imageProcessor = ImageProcessing(photo)
        }

        return imageProcessor.estimateAge()
    }

    fun pixelateImage(bitmap: Bitmap) {
        loading.value = true
        pixelatedImage.value = ImagePixelizator.pixelateImage(bitmap)
        loading.value = false
    }

    fun grayscaleImage(bitmap: Bitmap): Bitmap {
        return ImagePixelizator.grayscaleImage(bitmap)
    }

}