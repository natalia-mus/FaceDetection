package com.example.facedetection.model

import android.util.Log
import com.example.facedetection.model.datamodel.facesinfo.FacesInfo
import com.example.facedetection.model.datamodel.facesinfo.Tag

object ImageProcessing {

    fun processImage(facesInfo: FacesInfo) {
        val face = facesInfo.photos[0].tags
        val peopleCount = face.size

        Log.e("people", peopleCount.toString())
        checkAge(face)
    }

    fun checkAge(face: List<Tag>) {

        for (element in face) {
            val age = element.attributes.ageEst.value.toInt()
            if (age < 16) {
                Log.e("wiek", "dziecko")
            } else {
                Log.e("wiek", "dorosły")
            }
        }
    }

}