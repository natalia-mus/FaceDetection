package com.example.facedetection.api.facesinfo

import android.util.Log
import com.example.facedetection.api.RepositoryCallback
import com.example.facedetection.model.datamodel.apiusage.APIUsage
import com.example.facedetection.model.datamodel.facesinfo.FacesInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// http://api.skybiometry.com/fc/faces/ detect.json ? api_key=i8dh45sw5 & api_secret=oa8d2sm9f & urls=https://www.example.pl/uploads/media/default/e04a287270b.jpeg & attributes=age

object FacesInfoRepository {

    private const val BASE_URL = "http://api.skybiometry.com/fc/"
    private const val API_KEY = "uece7en7b1n1mbd5uuo2fq7p7i"
    private const val API_SECRET = "skrmgdhpk62rld7dsju3s9m0g7"
    private const val ATTRIBUTES = "age,gender"

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val apiService = retrofit.create(FacesInfoAPIService::class.java)

    fun getAPIUsageInfo(callback: RepositoryCallback<APIUsage>) {
        apiService.getAPIUsage(API_KEY, API_SECRET).enqueue(object : Callback<APIUsage> {
            override fun onResponse(call: Call<APIUsage>, response: Response<APIUsage>) {
                response.body()?.let { callback.onSuccess(it) }
            }

            override fun onFailure(call: Call<APIUsage>, t: Throwable) {
                Log.e("FacesInfoRepository.getAPIUsageInfo", t.message.toString())
                callback.onError()
            }
        })
    }

    fun getFacesInfo(url: String, callback: RepositoryCallback<FacesInfo>) {
        apiService.getFacesInfo(API_KEY, API_SECRET, url, ATTRIBUTES)
            .enqueue(object : Callback<FacesInfo> {
                override fun onResponse(call: Call<FacesInfo>, response: Response<FacesInfo>) {
                    response.body()?.let { callback.onSuccess(it) }
                }

                override fun onFailure(call: Call<FacesInfo>, t: Throwable) {
                    Log.e("FacesInfoRepository.getFacesInfo", t.message.toString())
                    callback.onError()
                }
            })
    }

}