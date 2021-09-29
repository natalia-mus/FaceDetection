package com.example.facedetection.model

import android.graphics.*
import com.example.facedetection.model.datamodel.facesinfo.Photo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL

class ImageProcessing(private val photo: Photo) {

    private val faces = photo.tags

    fun countPeople() = faces.size

    fun countAdults(): Int {
        var adultsCount = 0

        for (face in faces) {
            if (face.attributes.ageEst.value.toInt() >= 16) adultsCount++
        }

        return adultsCount
    }

    fun countChildren(): Int {
        var childrenCount = 0

        for (face in faces) {
            if (face.attributes.ageEst.value.toInt() < 16) childrenCount++
        }

        return childrenCount
    }


    fun drawRectangles(): Bitmap {
        val urlPhoto = photo.url
        val url = URL(urlPhoto)

        val photoWidth = photo.width
        val photoHeight = photo.height

        val bitmap = Bitmap.createBitmap(photoWidth, photoHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f

        GlobalScope.launch {
            val photoBitmap = getImageFromURL(url)
            canvas.drawBitmap(photoBitmap, Matrix(), null)

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
        }

        return bitmap
    }

    private suspend fun getImageFromURL(url: URL): Bitmap {
        val bitmap =
            GlobalScope.async { BitmapFactory.decodeStream(url.openConnection().getInputStream()) }
        return bitmap.await()
    }

}