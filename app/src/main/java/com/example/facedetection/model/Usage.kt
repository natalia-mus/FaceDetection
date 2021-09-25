package com.example.facedetection.model


import com.google.gson.annotations.SerializedName

data class Usage(
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("remaining")
    val remaining: Int,
    @SerializedName("reset_time")
    val resetTime: Int,
    @SerializedName("reset_time_text")
    val resetTimeText: String,
    @SerializedName("used")
    val used: Int
)