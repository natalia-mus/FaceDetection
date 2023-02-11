package com.example.facedetection.api.imagetourl

import android.util.Log
import com.example.facedetection.Settings
import com.example.facedetection.api.RepositoryCallback
import com.example.facedetection.model.datamodel.imagetourl.APIKeyConfirmationResult
import com.example.facedetection.model.datamodel.imagetourl.ImageToUrl
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ImageToUrlRepository {

    private const val BASE_URL = "https://api.imgbb.com/"
    private const val API_KEY = "71d60985b5f8dd4f6f423458a7ea70cf"
    private const val IMAGE = "image"

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val apiService = retrofit.create(ImageToUrlAPIService::class.java)


    fun getImageUrl(image: String, callback: RepositoryCallback<ImageToUrl>) {
        val body = MultipartBody.Part.createFormData(IMAGE, image)
        val apiKey = getAPIKey()

        apiService.getImageUrl(apiKey, body).enqueue(object : Callback<ImageToUrl> {
            override fun onResponse(call: Call<ImageToUrl>, response: Response<ImageToUrl>) {
                response.body()?.let { callback.onSuccess(it) }
            }

            override fun onFailure(call: Call<ImageToUrl>, t: Throwable) {
                Log.e("error", t.message.toString())
                callback.onError()
            }
        })
    }

    fun confirmAPIKey(apiKey: String, callback: RepositoryCallback<APIKeyConfirmationResult>): Boolean {
        apiService.confirmAPIKey(apiKey).enqueue(object : Callback<APIKeyConfirmationResult> {
            override fun onResponse(call: Call<APIKeyConfirmationResult>, response: Response<APIKeyConfirmationResult>) {
                if (response.errorBody() != null) {
                    val result = handleAPIKeyConfirmationResult(response.errorBody()!!)

                    if (result != null) {
                        callback.onSuccess(result)

                    } else callback.onError()
                }
            }

            override fun onFailure(call: Call<APIKeyConfirmationResult>, t: Throwable) {
                Log.e("error", t.message.toString())
                callback.onError()
            }
        })

        return true
    }

    private fun handleAPIKeyConfirmationResult(response: ResponseBody): APIKeyConfirmationResult? {
        val converter = retrofit.responseBodyConverter<APIKeyConfirmationResult>(APIKeyConfirmationResult::class.java, arrayOfNulls<Annotation>(0))
        return converter.convert(response)
    }

    /**
     * Returns user's API key if it's defined by user - otherwise returns default API key
     */
    private fun getAPIKey(): String {
        val apiKey = Settings.getAPIKey()

        return apiKey.ifEmpty {
            API_KEY
        }
    }

}