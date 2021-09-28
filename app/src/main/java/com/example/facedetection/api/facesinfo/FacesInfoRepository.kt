package com.example.facedetection.api.facesinfo

import android.util.Log
import com.example.facedetection.api.RepositoryCallback
import com.example.facedetection.model.datamodel.facesinfo.FacesInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// http://api.skybiometry.com/fc/faces/ detect.json ? api_key=ir36vvdkebnvbucctdnmke653d & api_secret=taubtcdfrtihh83i5pchqkdbbh & urls=https://www.elleman.pl/uploads/media/default/0005/40/cec344175ad1976fe2a78e04a2843f97ce77270b.jpeg & attributes=age

//private const val API_KEY = "ir36vvdkebnvbucctdnmke653d"
//private const val API_SECRET = "taubtcdfrtihh83i5pchqkdbbh"


object FacesInfoRepository {

    private const val BASE_URL = "http://api.skybiometry.com/fc/faces/"

    private const val RESPONSE_FORMAT = "detect.json"
    private const val API_KEY = "uece7en7b1n1mbd5uuo2fq7p7i"
    private const val API_SECRET = "skrmgdhpk62rld7dsju3s9m0g7"
    private const val ATTRIBUTES = "age"

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val apiService = retrofit.create(FacesInfoAPIService::class.java)

    fun getFacesInfo(url: String, callback: RepositoryCallback<FacesInfo>) {
        apiService.getFacesInfo(RESPONSE_FORMAT, API_KEY, API_SECRET, url, ATTRIBUTES)
            .enqueue(object : Callback<FacesInfo> {
                override fun onResponse(call: Call<FacesInfo>, response: Response<FacesInfo>) {
                    response.body()?.let { callback.onSuccess(it) }
                }

                override fun onFailure(call: Call<FacesInfo>, t: Throwable) {
                    Log.e("error", t.message.toString())
                    callback.onError()
                }
            })
    }

}