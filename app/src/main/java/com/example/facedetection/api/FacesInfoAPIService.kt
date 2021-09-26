package com.example.facedetection.api

import com.example.facedetection.model.FacesInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FacesInfoAPIService {

    // http://api.skybiometry.com/fc/faces/ detect.json ? api_key=ir36vvdkebnvbucctdnmke653d & api_secret=taubtcdfrtihh83i5pchqkdbbh & urls=https://www.elleman.pl/uploads/media/default/0005/40/cec344175ad1976fe2a78e04a2843f97ce77270b.jpeg & attributes=age

    @GET("{responseFormat}")
    fun getFacesInfo(
        @Path("responseFormat") responseFormat: String,
        @Query("api_key") apiKey: String,
        @Query("api_secret") apiSecret: String,
        @Query("urls") urls: String,
        @Query("attributes") attributes: String
    ): Call<FacesInfo>

}