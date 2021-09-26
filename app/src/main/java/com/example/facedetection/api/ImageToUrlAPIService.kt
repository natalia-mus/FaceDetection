package com.example.facedetection.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ImageToUrlAPIService {

    // https://api.imgbb.com/1/upload?key=71d60985b5f8dd4f6f423458a7ea70cf&image=R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7

    @GET("{upload}")
    fun getImageUrl(@Path("upload") upload: String, @Query("key") key: String, @Query("image") image: String): Call<>
}