package com.example.facedetection.model.datamodel.facesinfo


import com.google.gson.annotations.SerializedName

data class EyeLeft(
    @SerializedName("confidence")
    val confidence: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("x")
    val x: Double,
    @SerializedName("y")
    val y: Double
)