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
    val imageExpirationValue = MutableLiveData<Boolean>()
    val apiKeyConfirmationStatus = MutableLiveData<APIKeyConfirmationStatus>()


    fun getAPISettings() {
        getAPIKey()
        getImageExpirationValue()
    }

    fun getAPIKey() {
        apiKey.value = Settings.getAPIKey()
    }

    fun getImageExpirationValue() {
        imageExpirationValue.value = Settings.getImageExpirationTime() != 0
    }

    fun saveChanges(apiKey: String, setImageExpirationTime: Boolean) {
        loading.value = true

        if (apiKey == "") {
            loading.value = false
            apiKeyConfirmationStatus.value = APIKeyConfirmationStatus.Empty

        } else if (apiKey == this.apiKey.value && setImageExpirationTime == imageExpirationValue.value) {
            loading.value = false
            apiKeyConfirmationStatus.value = APIKeyConfirmationStatus.Valid

        } else if (apiKey == this.apiKey.value && setImageExpirationTime != imageExpirationValue.value) {
            saveSetImageExpirationTimeValue(setImageExpirationTime)
            loading.value = false
            apiKeyConfirmationStatus.value = APIKeyConfirmationStatus.Valid

        } else {
            ImageToUrlRepository.confirmAPIKey(
                apiKey,
                object : RepositoryCallback<APIKeyConfirmationResult> {
                    override fun onSuccess(data: APIKeyConfirmationResult) {
                        loading.value = false

                        if (data.error.code != ErrorCode.InvalidAPIKey.code) {
                            Settings.saveAPIKey(apiKey)
                            saveSetImageExpirationTimeValue(setImageExpirationTime)
                            apiKeyConfirmationStatus.value = APIKeyConfirmationStatus.Valid
                        } else {
                            apiKeyConfirmationStatus.value = APIKeyConfirmationStatus.Invalid
                        }
                    }

                    override fun onError() {
                        loading.value = false
                        apiKeyConfirmationStatus.value = APIKeyConfirmationStatus.Error
                    }
                })
        }
    }

    private fun saveSetImageExpirationTimeValue(setImageExpirationTime: Boolean) {
        if (setImageExpirationTime) {
            Settings.saveExpirationTime(60)
        } else {
            Settings.saveExpirationTime(0)
        }
    }
}


enum class APIKeyConfirmationStatus {
    Valid,
    Invalid,
    Empty,
    Error
}