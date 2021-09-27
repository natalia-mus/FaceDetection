package com.example.facedetection.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.facedetection.api.FacesInfoRepository
import com.example.facedetection.api.ImageToUrlRepository
import com.example.facedetection.api.RepositoryCallback
import com.example.facedetection.model.facesinfo.FacesInfo
import com.example.facedetection.model.imagetourl.ImageToUrl

class MainActivityViewModel : ViewModel() {

    val facesInfo = MutableLiveData<FacesInfo>()

    fun detectFaces(image: String) {
        ImageToUrlRepository.getImageUrl(image, object : RepositoryCallback<ImageToUrl> {
            override fun onSuccess(data: ImageToUrl) {
                val url = data.data.url
                getFacesInfo(url)
            }

            override fun onError() {
                Log.e("detectFaces", "error")
            }
        })
    }


    private fun getFacesInfo(url: String) {
        FacesInfoRepository.getFacesInfo(url, object : RepositoryCallback<FacesInfo> {
            override fun onSuccess(data: FacesInfo) {
                Log.e("success", data.toString())
            }

            override fun onError() {
                Log.e("getFacesInfo", "error")
            }
        })
    }

}