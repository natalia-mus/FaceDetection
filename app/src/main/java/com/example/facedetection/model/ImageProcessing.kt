package com.example.facedetection.model

import android.util.Log
import com.example.facedetection.model.datamodel.facesinfo.FacesInfo
import com.example.facedetection.model.datamodel.facesinfo.Tag

// Tag >> center, width, height

object ImageProcessing {

    fun processImage(facesInfo: FacesInfo) {
        val faces = facesInfo.photos[0].tags

        countPeople(faces)
        //checkAge(faces)
        checkFaceCoordinates(faces)
    }

    private fun countPeople(faces: List<Tag>) {
        val peopleCount = faces.size
        Log.e("people", peopleCount.toString())
    }

    /*private fun checkAge(faces: List<Tag>) {

        for (element in faces) {
            val age = element.attributes.ageEst.value.toInt()
            if (age < 16) {
                Log.e("age", "child")
            } else {
                Log.e("age", "adult")
            }
        }
    }*/

    private fun checkFaceCoordinates(faces: List<Tag>) {

        for (element in faces) {
            val center = element.center
            val width = element.width
            val height = element.height
            val age = element.attributes.ageEst.value

            Log.e("center", center.toString())
            Log.e("width", width.toString())
            Log.e("height", height.toString())
            Log.e("age", age)
        }
    }

}