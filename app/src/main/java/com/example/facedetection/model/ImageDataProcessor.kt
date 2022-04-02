package com.example.facedetection.model

import android.content.Context
import android.graphics.*
import androidx.core.graphics.scale
import com.example.facedetection.R
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

        val paintBlack = Paint(Paint.ANTI_ALIAS_FLAG)
        paintBlack.color = Color.BLACK

        val paintWhite = Paint(Paint.ANTI_ALIAS_FLAG)
        paintWhite.color = Color.WHITE

        GlobalScope.launch {
            val photoBitmap = getImageFromURL(url)
            canvas.drawBitmap(photoBitmap, Matrix(), null)

            for (face in faces) {
                val age = face.attributes.ageEst.value.toInt()

                val centerX = face.center.x.toFloat()
                val centerY = face.center.y.toFloat() + (face.center.y / 2).toFloat()

                val width = face.width.toFloat()
                val height = face.height.toFloat() / 2

                paintBlack.textSize = height * 5

                // rectangle stroke
                var left = (((centerX + width / 2) * photoWidth) / 100).toInt()
                var right = (((centerX - width / 2) * photoWidth) / 100).toInt()
                var top = (((centerY - height / 2) * photoHeight) / 100).toInt()
                var bottom = (((centerY + height / 2) * photoHeight) / 100).toInt()

                var rect = Rect(left, top, right, bottom)
                canvas.drawRect(rect, paintBlack)

                // rectangle
                left -= 10 / 8
                right += 10 / 8
                top += 10 / 8
                bottom -= 10 / 8
                rect = Rect(left, top, right, bottom)
                canvas.drawRect(rect, paintWhite)

                // text
                val x = rect.right + (rect.centerX() - rect.right) / 2
                val y = rect.bottom + (rect.centerY() - rect.bottom) / 2
                canvas.drawText(
                    age.toString(),
                    x.toFloat(),
                    y.toFloat(),
                    paintBlack
                )
            }
        }

        return bitmap
    }


    fun getGender(context: Context): Bitmap {
        val urlPhoto = photo.url
        val url = URL(urlPhoto)

        val photoWidth = photo.width
        val photoHeight = photo.height

        val bitmap = Bitmap.createBitmap(photoWidth, photoHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paintTransparent = Paint(Paint.ANTI_ALIAS_FLAG)
        paintTransparent.color = Color.TRANSPARENT

        val paintBlack = Paint(Paint.ANTI_ALIAS_FLAG)
        paintBlack.color = Color.BLACK

        GlobalScope.launch {
            val photoBitmap = getImageFromURL(url)
            canvas.drawBitmap(photoBitmap, Matrix(), null)

            for (face in faces) {
                var genderSign = BitmapFactory.decodeResource(context.resources, R.drawable.gender_female)

                val centerX = face.center.x.toFloat()
                val centerY = face.center.y.toFloat() + (face.center.y / 2).toFloat()

                val width = face.width.toFloat()
                val height = face.height.toFloat() / 2

                // transparent helper
                var left = (((centerX + width / 2) * photoWidth) / 100).toInt()
                var right = (((centerX - width / 2) * photoWidth) / 100).toInt()
                var top = (((centerY - height / 2) * photoHeight) / 100).toInt()
                var bottom = (((centerY + height / 2) * photoHeight) / 100).toInt()

                // we want to draw helper and gender sign below detected face, not exactly on it
                left -= 10 / 8
                right += 10 / 8
                top += 10 / 8
                bottom -= 10 / 8
                val rect = Rect(left, top, right, bottom)
                canvas.drawRect(rect, paintTransparent)

                // gender sign
                val x = rect.right + (rect.centerX() - rect.right) / 2
                val y = rect.top + (rect.top / 8)

                val scale = rect.height()
                genderSign = genderSign.scale(scale, scale, true)
                canvas.drawBitmap(genderSign, x.toFloat(), y.toFloat(), paintBlack)
            }
        }

        return bitmap
    }


    private suspend fun getImageFromURL(url: URL): Bitmap {
        val bitmap = GlobalScope.async { BitmapFactory.decodeStream(url.openConnection().getInputStream()) }
        return bitmap.await()
    }

}