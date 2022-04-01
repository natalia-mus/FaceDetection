package com.example.facedetection.api.facesinfo

import com.example.facedetection.model.datamodel.apiusage.APIUsage
import com.example.facedetection.model.datamodel.facesinfo.FacesInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FacesInfoAPIService {

    // http://api.skybiometry.com/fc/faces/ detect.json ? api_key=i8dh45sw5 & api_secret=oa8d2sm9f & urls=https://www.example.pl/uploads/media/default/e04a287270b.jpeg & attributes=age

    // http://api.skybiometry.com/fc/account/limits.json ? api_key=i8dh45sw5 & api_secret=oa8d2sm9f

    @GET("account/limits.json")
    fun getAPIUsage(
        @Query("api_key") apiKey: String,
        @Query("api_secret") apiSecret: String
    ): Call<APIUsage>


    @GET("faces/detect.json")
    fun getFacesInfo(
        @Query("api_key") apiKey: String,
        @Query("api_secret") apiSecret: String,
        @Query("urls") urls: String,
        @Query("attributes") attributes: String
    ): Call<FacesInfo>

}