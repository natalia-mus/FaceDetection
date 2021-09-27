package com.example.facedetection.model.facesinfo


import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("height")
    val height: Int,
    @SerializedName("pid")
    val pid: String,
    @SerializedName("tags")
    val tags: List<Tag>,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)