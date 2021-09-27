package com.example.facedetection.model.datamodel.facesinfo


import com.google.gson.annotations.SerializedName

data class Center(
    @SerializedName("x")
    val x: Double,
    @SerializedName("y")
    val y: Double
)