package com.example.facedetection.model.datamodel.facesinfo


import com.google.gson.annotations.SerializedName

data class AgeEst(
    @SerializedName("confidence")
    val confidence: Int,
    @SerializedName("value")
    val value: String
)