package com.example.facedetection.api.imagetourl

import com.example.facedetection.model.datamodel.imagetourl.APIKeyConfirmationResult
import com.example.facedetection.model.datamodel.imagetourl.ImageToUrl
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ImageToUrlAPIService {

    // https://api.imgbb.com/1/upload?key=71d60985b5f8dd4f6f423458a7ea70cf&image=R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7

    @Multipart
    @POST("1/upload")
    fun getImageUrl(
        @Query("expiration") expiration: String,
        @Query("key") key: String,
        @Part part: MultipartBody.Part
    ): Call<ImageToUrl>

    @POST("1/upload")
    fun confirmAPIKey(@Query("key") key: String): Call<APIKeyConfirmationResult>
}