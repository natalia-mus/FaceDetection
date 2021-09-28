package com.example.facedetection.model.datamodel.facesinfo


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
): Parcelable