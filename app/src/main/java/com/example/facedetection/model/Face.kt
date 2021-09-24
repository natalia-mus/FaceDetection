package com.example.facedetection.model


import com.google.gson.annotations.SerializedName

data class Face(
    @SerializedName("confidence")
    val confidence: Int,
    @SerializedName("value")
    val value: String
)