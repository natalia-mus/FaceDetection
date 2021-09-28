package com.example.facedetection.model.datamodel.facesinfo


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tag(
    @SerializedName("attributes")
    val attributes: Attributes,
    @SerializedName("center")
    val center: Center,
    @SerializedName("height")
    val height: Double,
    @SerializedName("width")
    val width: Double
) : Parcelable