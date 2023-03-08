package com.example.facedetection.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.facedetection.ErrorCode
import com.example.facedetection.Settings
import com.example.facedetection.api.RepositoryCallback
import com.example.facedetection.api.imagetourl.ImageToUrlRepository
import com.example.facedetection.model.datamodel.imagetourl.APIKeyConfirmationResult

class ChangeAPIKeyViewModel : ViewModel() {

    val loading = MutableLiveData<Boolean>()
    val apiKey = MutableLiveData<String>()
    val isAPIKeyValid = MutableLiveData<APIKeyConfirmationStatus>()


    fun getAPIKey() {
        apiKey.value = Settings.getAPIKey()
    }

    fun saveAPIKey(apiKey: String) {
        loading.value = true

        if (apiKey == "") {
            loading.value = false
            Settings.saveAPIKey(apiKey)
            isAPIKeyValid.value = APIKeyConfirmationStatus.Default

        } else {
            ImageToUrlRepository.confirmAPIKey(
                apiKey,
                object : RepositoryCallback<APIKeyConfirmationResult> {
                    override fun onSuccess(data: APIKeyConfirmationResult) {
                        loading.value = false

                        if (data.error.code != ErrorCode.InvalidAPIKey.code) {
                            Settings.saveAPIKey(apiKey)
                            isAPIKeyValid.value = APIKeyConfirmationStatus.Valid
                        } else {
                            isAPIKeyValid.value = APIKeyConfirmationStatus.Invalid
                        }
                    }

                    override fun onError() {
                        loading.value = false
                        isAPIKeyValid.value = APIKeyConfirmationStatus.Error
                    }
                })
        }
    }
}


enum class APIKeyConfirmationStatus {
    Valid,
    Invalid,
    Default,
    Error
}