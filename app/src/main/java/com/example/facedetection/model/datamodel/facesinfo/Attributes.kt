package com.example.facedetection.model.datamodel.facesinfo


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Attributes(
    @SerializedName("age_est")
    val ageEst: AgeEst,
    /*@SerializedName("face")
    val face: Face*/
): Parcelable