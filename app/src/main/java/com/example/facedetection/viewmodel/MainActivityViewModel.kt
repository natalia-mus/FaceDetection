package com.example.facedetection.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.facedetection.api.FacesInfoRepository
import com.example.facedetection.api.ImageToUrlRepository
import com.example.facedetection.api.RepositoryCallback
import com.example.facedetection.model.ImageProcessing
import com.example.facedetection.model.datamodel.facesinfo.FacesInfo
import com.example.facedetection.model.datamodel.imagetourl.ImageToUrl

class MainActivityViewModel : ViewModel() {

    val loading = MutableLiveData<Boolean>(false)
    val facesInfo = MutableLiveData<FacesInfo>()

    fun detectFaces(image: String) {
        loading.value = true

        ImageToUrlRepository.getImageUrl(image, object : RepositoryCallback<ImageToUrl> {
            override fun onSuccess(data: ImageToUrl) {
                val url = data.data.url
                getFacesInfo(url)
            }

            override fun onError() {
                Log.e("detectFaces", "error")
                loading.value = false
            }
        })
    }


    private fun getFacesInfo(url: String) {
        FacesInfoRepository.getFacesInfo(url, object : RepositoryCallback<FacesInfo> {
            override fun onSuccess(data: FacesInfo) {
                Log.e("success", data.toString())
                loading.value = false
                facesInfo.value = data
                processImage(data)
            }

            override fun onError() {
                Log.e("getFacesInfo", "error")
                loading.value = false
            }
        })
    }


    private fun processImage(facesInfo: FacesInfo) {
        Log.e("process", facesInfo.toString())
        ImageProcessing.processImage(facesInfo)
    }

}