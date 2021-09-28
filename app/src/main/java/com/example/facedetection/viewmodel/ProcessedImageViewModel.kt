package com.example.facedetection.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.facedetection.model.ImageProcessing
import com.example.facedetection.model.datamodel.facesinfo.Photo

class ProcessedImageViewModel : ViewModel() {

    val peopleCount = MutableLiveData<Int>()
    val childrenCount = MutableLiveData<Int>()
    val adultsCount = MutableLiveData<Int>()
    val processedImage = MutableLiveData<Bitmap>()

    fun processImage(photo: Photo) {
        val faces = photo.tags

        peopleCount.value = ImageProcessing.countPeople(faces)
        adultsCount.value = ImageProcessing.countAdults(faces)
        childrenCount.value = ImageProcessing.countChildren(faces)
        processedImage.value = ImageProcessing.drawRectangles(photo)
    }
}