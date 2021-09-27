package com.example.facedetection.api

import android.util.Log
import com.example.facedetection.model.imagetourl.ImageToUrl
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ImageToUrlRepository {

    private const val BASE_URL = "https://api.imgbb.com/"
    private const val API_KEY = "71d60985b5f8dd4f6f423458a7ea70cf"

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val apiService = retrofit.create(ImageToUrlAPIService::class.java)

    fun getImageUrl(image: String, callback: RepositoryCallback<ImageToUrl>) {
        val body = MultipartBody.Part.createFormData("image", image)

        apiService.getImageUrl(API_KEY, body).enqueue(object : Callback<ImageToUrl> {
            override fun onResponse(call: Call<ImageToUrl>, response: Response<ImageToUrl>) {
                Log.e("request", response.raw().request().url().toString())
                Log.e("response", response.body().toString())
            }

            override fun onFailure(call: Call<ImageToUrl>, t: Throwable) {
                Log.e("error", "error")
            }
        })
    }

}