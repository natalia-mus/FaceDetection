package com.example.facedetection.model.facesinfo


import com.google.gson.annotations.SerializedName

data class Tag(
    @SerializedName("attributes")
    val attributes: Attributes,
    @SerializedName("center")
    val center: Center,
    @SerializedName("confirmed")
    val confirmed: Boolean,
    @SerializedName("eye_left")
    val eyeLeft: EyeLeft,
    @SerializedName("eye_right")
    val eyeRight: EyeRight,
    @SerializedName("height")
    val height: Double,
    @SerializedName("label")
    val label: Any,
    @SerializedName("manual")
    val manual: Boolean,
    @SerializedName("mouth_center")
    val mouthCenter: MouthCenter,
    @SerializedName("nose")
    val nose: Nose,
    @SerializedName("pitch")
    val pitch: Int,
    @SerializedName("points")
    val points: Any,
    @SerializedName("recognizable")
    val recognizable: Boolean,
    @SerializedName("roll")
    val roll: Int,
    @SerializedName("similarities")
    val similarities: Any,
    @SerializedName("tid")
    val tid: String,
    @SerializedName("uids")
    val uids: List<Any>,
    @SerializedName("width")
    val width: Double,
    @SerializedName("yaw")
    val yaw: Int
)