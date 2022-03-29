package com.example.facedetection.model.datamodel.apiusage


import com.google.gson.annotations.SerializedName

data class APIUsage(
    @SerializedName("status")
    val status: String,
    @SerializedName("usage")
    val usage: Usage
)