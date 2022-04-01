package com.example.facedetection.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.facedetection.Status
import com.example.facedetection.api.RepositoryCallback
import com.example.facedetection.api.facesinfo.FacesInfoRepository
import com.example.facedetection.api.imagetourl.ImageToUrlRepository
import com.example.facedetection.model.datamodel.apiusage.APIUsage
import com.example.facedetection.model.datamodel.apiusage.APIUsageData
import com.example.facedetection.model.datamodel.facesinfo.FacesInfo
import com.example.facedetection.model.datamodel.imagetourl.ImageToUrl
import kotlin.math.roundToInt

class MainActivityViewModel : ViewModel() {

    val loading = MutableLiveData<Boolean>(false)
    val apiUsage = MutableLiveData<APIUsageData>()
    val facesInfo = MutableLiveData<FacesInfo>()
    val status = MutableLiveData<Status>(Status.UNKNOWN)


    fun checkAPIUsage() {
        loading.value = true
        status.value = Status.UNKNOWN

        FacesInfoRepository.getAPIUsageInfo(object : RepositoryCallback<APIUsage> {
            override fun onSuccess(data: APIUsage) {
                val result = APIUsageData(
                    data.usage.limit,
                    data.usage.remaining,
                    data.usage.used,
                    countPercentageAPIUsage(data)
                )
                loading.value = false
                apiUsage.value = result
                status.value = Status.SUCCESS
            }

            override fun onError() {
                loading.value = false
                status.value = Status.ERROR
            }
        })
    }


    fun detectFaces(image: String) {
        loading.value = true
        status.value = Status.UNKNOWN

        ImageToUrlRepository.getImageUrl(image, object : RepositoryCallback<ImageToUrl> {
            override fun onSuccess(data: ImageToUrl) {
                val url = data.data.url
                getFacesInfo(url)
            }

            override fun onError() {
                loading.value = false
                status.value = Status.ERROR
            }
        })
    }


    private fun getFacesInfo(url: String) {
        FacesInfoRepository.getFacesInfo(url, object : RepositoryCallback<FacesInfo> {
            override fun onSuccess(data: FacesInfo) {
                loading.value = false
                facesInfo.value = data
                status.value = Status.SUCCESS
            }

            override fun onError() {
                loading.value = false
                status.value = Status.ERROR
            }
        })
    }


    private fun countPercentageAPIUsage(apiUsage: APIUsage): Int {
        val used = apiUsage.usage.used.toFloat()
        val limit = apiUsage.usage.limit.toFloat()
        return (used / limit * 100).roundToInt()
    }

}