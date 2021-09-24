package com.example.facedetection.model


import com.google.gson.annotations.SerializedName

data class FacesInfo(
    @SerializedName("operation_id")
    val operationId: String,
    @SerializedName("photos")
    val photos: List<Photo>,
    @SerializedName("status")
    val status: String,
    @SerializedName("usage")
    val usage: Usage
)