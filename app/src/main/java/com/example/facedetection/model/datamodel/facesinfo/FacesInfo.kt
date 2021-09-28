package com.example.facedetection.model.datamodel.facesinfo


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FacesInfo(
    @SerializedName("operation_id")
    val operationId: String,
    @SerializedName("photos")
    val photos: List<Photo>,
    @SerializedName("status")
    val status: String,
    /*@SerializedName("usage")
    val usage: Usage*/
): Parcelable