package com.example.facedetection.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.facedetection.api.FacesInfoRepository
import com.example.facedetection.api.RepositoryCallback
import com.example.facedetection.model.FacesInfo

class MainActivityViewModel : ViewModel() {

    val facesInfo = MutableLiveData<FacesInfo>()

    fun getFacesInfo(url: String) {
        FacesInfoRepository.getFacesInfo(url, object : RepositoryCallback<FacesInfo> {
            override fun onSuccess(data: FacesInfo) {
                Log.e("success", data.toString())
            }

            override fun onError() {
                Log.e("error", "error")
            }
        })
    }

}