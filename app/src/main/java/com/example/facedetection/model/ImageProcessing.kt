package com.example.facedetection.model

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.facedetection.model.datamodel.facesinfo.Photo
import com.example.facedetection.model.datamodel.facesinfo.Tag

object ImageProcessing {

    fun countPeople(faces: List<Tag>) = faces.size

    fun countChildren(faces: List<Tag>): Int {
        var childrenCount = 0

        for (face in faces) {
            if (face.attributes.ageEst.value.toInt() < 16) childrenCount++
        }

        return childrenCount
    }

    fun countAdults(faces: List<Tag>): Int {
        var adultsCount = 0

        for (face in faces) {
            if (face.attributes.ageEst.value.toInt() >= 16) adultsCount++
        }

        return adultsCount
    }

    fun drawRectangles(photo: Photo): Bitmap {
        val photoWidth = photo.width
        val photoHeight = photo.height

        val faces = photo.tags

        val bitmap = Bitmap.createBitmap(photoWidth, photoHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f

        for (face in faces) {
            if (face.attributes.ageEst.value.toInt() < 16) {
                paint.color = Color.BLUE
            } else {
                paint.color = Color.GREEN
            }

            val centerX = face.center.x.toFloat()
            val centerY = face.center.y.toFloat()

            val width = face.width.toFloat()
            val height = face.height.toFloat()

            val left = ((centerX + width / 2) * photoWidth) / 100
            val right = ((centerX - width / 2) * photoWidth) / 100
            val top = ((centerY - height / 2) * photoHeight) / 100
            val bottom = ((centerY + height / 2) * photoHeight) / 100

            canvas.drawRect(left, top, right, bottom, paint)

        }

        return bitmap
    }

}