package com.example.facedetection.model.datamodel.facesinfo


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AgeEst(
    /*@SerializedName("confidence")
    val confidence: Int,*/
    @SerializedName("value")
    val value: String
): Parcelable