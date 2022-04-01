package com.example.facedetection.model.datamodel.apiusage


import com.google.gson.annotations.SerializedName

data class Usage(
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("namespace_limit")
    val namespaceLimit: Int,
    @SerializedName("namespace_used")
    val namespaceUsed: Int,
    @SerializedName("remaining")
    val remaining: Int,
    @SerializedName("reset_time")
    val resetTime: Int,
    @SerializedName("reset_time_text")
    val resetTimeText: String,
    @SerializedName("used")
    val used: Int
)