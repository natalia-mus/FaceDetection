package com.example.facedetection.model


import com.google.gson.annotations.SerializedName

data class EyeRight(
    @SerializedName("confidence")
    val confidence: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("x")
    val x: Double,
    @SerializedName("y")
    val y: Double
)