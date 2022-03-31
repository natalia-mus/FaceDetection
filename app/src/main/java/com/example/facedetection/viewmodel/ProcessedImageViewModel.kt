package com.example.facedetection.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.facedetection.model.ImageProcessing
import com.example.facedetection.model.datamodel.facesinfo.Photo
import com.example.facedetection.util.ImagePixelizator

class ProcessedImageViewModel : ViewModel() {

    val peopleCount = MutableLiveData<Int>()
    val adultsCount = MutableLiveData<Int>()
    val childrenCount = MutableLiveData<Int>()
    val processedImage = MutableLiveData<Bitmap>()

    fun processImage(photo: Photo) {
        val model = ImageProcessing(photo)

        peopleCount.value = model.countPeople()
        adultsCount.value = model.countAdults()
        childrenCount.value = model.countChildren()
        processedImage.value = model.drawRectangles()
    }

    fun pixelateImage(bitmap: Bitmap): Bitmap {
        return ImagePixelizator.pixelateImage(bitmap)
    }

    fun grayscaleImage(bitmap: Bitmap): Bitmap {
        return ImagePixelizator.grayscaleImage(bitmap)
    }

}