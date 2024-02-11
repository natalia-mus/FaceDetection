package com.example.facedetection.viewmodel

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.facedetection.model.ImageBitmapProcessor
import com.example.facedetection.model.ImageDataProcessor
import com.example.facedetection.model.datamodel.facesinfo.Photo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProcessedImageViewModel() : ViewModel() {

    val peopleCount = MutableLiveData<Int>()
    val adultsCount = MutableLiveData<Int>()
    val childrenCount = MutableLiveData<Int>()
    val processedImage = MutableLiveData<Bitmap>()
    val pixelatedImage = MutableLiveData<Bitmap>()

    val imageSavedSuccessfully = MutableLiveData<Boolean>()

    private val imageBitmapProcessor: ImageBitmapProcessor by lazy { ImageBitmapProcessor() }
    private lateinit var imageDataProcessor: ImageDataProcessor


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

    fun detectFaces(photo: Photo) {
        initDataProcessor(photo)

        GlobalScope.launch {
            processedImage.postValue(imageDataProcessor.detectFaces())
        }
    }

    fun estimateAge(photo: Photo) {
        initDataProcessor(photo)

        GlobalScope.launch {
            processedImage.postValue(imageDataProcessor.estimateAge())
        }
    }

    fun getGender(photo: Photo, resources: Resources) {
        initDataProcessor(photo)

        GlobalScope.launch {
            processedImage.postValue(imageDataProcessor.getGender(resources))
        }
    }

    fun pixelateImage(bitmap: Bitmap) {
        pixelatedImage.value = imageBitmapProcessor.pixelateImage(bitmap)
    }

    fun grayscaleImage(bitmap: Bitmap): Bitmap {
        return imageBitmapProcessor.grayscaleImage(bitmap)
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