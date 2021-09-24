package com.example.facedetection.model


import com.google.gson.annotations.SerializedName

data class Attributes(
    @SerializedName("age_est")
    val ageEst: AgeEst,
    @SerializedName("face")
    val face: Face
)