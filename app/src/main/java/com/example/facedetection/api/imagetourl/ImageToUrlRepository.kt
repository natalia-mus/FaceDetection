package com.example.facedetection.api.imagetourl

import android.util.Log
import com.example.facedetection.api.RepositoryCallback
import com.example.facedetection.model.datamodel.imagetourl.ImageToUrl
import okhttp3.MultipartBody
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

        apiService.getImageUrl(API_KEY, body).enqueue(object : Callback<ImageToUrl> {
            override fun onResponse(call: Call<ImageToUrl>, response: Response<ImageToUrl>) {
                response.body()?.let { callback.onSuccess(it) }
            }

            override fun onFailure(call: Call<ImageToUrl>, t: Throwable) {
                Log.e("error", t.message.toString())
                callback.onError()
            }
        })
    }

}