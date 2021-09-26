package com.example.facedetection.api

import com.example.facedetection.model.imagetourl.ImageToUrl
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ImageToUrlAPIService {

    // https://api.imgbb.com/1/upload?key=71d60985b5f8dd4f6f423458a7ea70cf&image=R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7

    @POST("{upload}")
    fun getImageUrl(
        @Path("upload") upload: String,
        @Query("key") key: String,
        @Query("image") image: String
    ): Call<ImageToUrl>
}