package com.example.facedetection.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.facedetection.Status
import com.example.facedetection.api.RepositoryCallback
import com.example.facedetection.api.facesinfo.FacesInfoRepository
import com.example.facedetection.api.imagetourl.ImageToUrlRepository
import com.example.facedetection.model.datamodel.facesinfo.FacesInfo
import com.example.facedetection.model.datamodel.imagetourl.ImageToUrl
import com.example.facedetection.util.ConstValues
import com.example.facedetection.util.ImageConverter

class MainActivityViewModel : ViewModel() {

    val loading = MutableLiveData<Boolean>(false)
    val facesInfo = MutableLiveData<FacesInfo>()
    val status = MutableLiveData<Status>(Status.UNKNOWN)

    fun detectFaces(bitmap: Bitmap) {
        loading.value = true
        status.value = Status.UNKNOWN

        if (correctPhotoSize(bitmap)) {
            status.value = Status.IN_PROGRESS
            val image = ImageConverter.convertToBase64(bitmap)
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
        } else {
            loading.value = false
            status.value = Status.PHOTO_TOO_LARGE
        }

    }


    private fun correctPhotoSize(bitmap: Bitmap): Boolean {
        return (bitmap.width * bitmap.height <= ConstValues.MAX_PHOTO_SIZE)
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

}