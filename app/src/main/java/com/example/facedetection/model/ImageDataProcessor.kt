package com.example.facedetection.model

import android.graphics.*
import com.example.facedetection.model.datamodel.facesinfo.Photo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL

class ImageDataProcessor(private val photo: Photo) {

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


    fun detectFaces(): Bitmap {
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


    fun estimateAge(): Bitmap {
        val urlPhoto = photo.url
        val url = URL(urlPhoto)

        val photoWidth = photo.width
        val photoHeight = photo.height

        val bitmap = Bitmap.createBitmap(photoWidth, photoHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        //backgroundPaint.color = Color.TRANSPARENT

        val textPaintBlack = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaintBlack.style = Paint.Style.FILL_AND_STROKE
        textPaintBlack.color = Color.BLACK
        backgroundPaint.typeface = Typeface.DEFAULT_BOLD

        val textPaintWhite = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaintBlack.style = Paint.Style.FILL
        textPaintWhite.color = Color.WHITE

        GlobalScope.launch {
            val photoBitmap = getImageFromURL(url)
            canvas.drawBitmap(photoBitmap, Matrix(), null)

            for (face in faces) {
                val age = face.attributes.ageEst.value.toInt()

                val centerX = face.center.x.toFloat()
                val centerY = face.center.y.toFloat() + (face.center.y / 2).toFloat()

                val width = face.width.toFloat()
                val height = face.height.toFloat() / 2

                var left = (((centerX + width / 2) * photoWidth) / 100).toInt()
                var right = (((centerX - width / 2) * photoWidth) / 100).toInt()
                var top = (((centerY - height / 2) * photoHeight) / 100).toInt()
                var bottom = (((centerY + height / 2) * photoHeight) / 100).toInt()

                textPaintBlack.textSize = height * 5
                textPaintWhite.textSize = height * 10

                var rect = Rect(left, top, right, bottom)
                canvas.drawRect(rect, textPaintBlack)

                left = ((((centerX + width / 2) * photoWidth) / 100) - 10 / 8).toInt()
                right = ((((centerX - width / 2) * photoWidth) / 100) + 10 / 8).toInt()
                top = ((((centerY - height / 2) * photoHeight) / 100) + 10 / 8).toInt()
                bottom = ((((centerY + height / 2) * photoHeight) / 100) - 10 / 8).toInt()
                rect = Rect(left, top, right, bottom)
                canvas.drawRect(rect, textPaintWhite)

                val x = rect.right + (rect.centerX() - rect.right)/2
                val y = rect.bottom + (rect.centerY() - rect.bottom)/2
                canvas.drawText(
                    age.toString(),
                    x.toFloat(),
                    y.toFloat(),
                    textPaintBlack
                )
                /*canvas.drawText(
                    age.toString(),
                    rect.centerX().toFloat(),
                    rect.centerY().toFloat(),
                    textPaintWhite
                )*/
            }
        }

        return bitmap
    }


    private suspend fun getImageFromURL(url: URL): Bitmap {
        val bitmap = GlobalScope.async { BitmapFactory.decodeStream(url.openConnection().getInputStream()) }
        return bitmap.await()
    }

}