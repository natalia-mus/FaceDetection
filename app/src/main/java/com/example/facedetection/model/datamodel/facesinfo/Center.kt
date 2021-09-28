package com.example.facedetection.model.datamodel.facesinfo


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Center(
    @SerializedName("x")
    val x: Double,
    @SerializedName("y")
    val y: Double
): Parcelable