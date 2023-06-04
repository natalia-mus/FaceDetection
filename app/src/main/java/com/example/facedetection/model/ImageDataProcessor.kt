package com.example.facedetection.model

import android.content.res.Resources
import android.graphics.*
import androidx.core.graphics.scale
import com.example.facedetection.R
import com.example.facedetection.model.datamodel.facesinfo.Photo
import com.example.facedetection.util.ConstValues
import kotlinx.coroutines.*
import java.net.URL

class ImageDataProcessor(private val photo: Photo) {

    private val faces = photo.tags


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

    fun countPeople() = faces.size

    fun detectFaces(): Bitmap {
        val photoWidth = photo.width
        val photoHeight = photo.height

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f

        val bitmap = getBitmap()

        GlobalScope.launch {
            val canvas = getCanvas(bitmap)

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
        val photoWidth = photo.width
        val photoHeight = photo.height

        val paintText = Paint(Paint.ANTI_ALIAS_FLAG)
        paintText.color = Color.BLACK

        val paintBackground = Paint(Paint.ANTI_ALIAS_FLAG)
        paintBackground.color = Color.WHITE
        paintBackground.alpha = 170

        val bitmap = getBitmap()

        GlobalScope.launch {
            val canvas = getCanvas(bitmap)

            for (face in faces) {
                val age = face.attributes.ageEst.value.toInt()

                val width = face.width.toFloat()
                val height = face.height.toFloat() / 2

                val centerX = face.center.x.toFloat()
                val centerY = face.center.y.toFloat() + (1.6 * height)

                paintText.textSize = height * 3

                // rectangle
                val left = (((centerX + width / 2) * photoWidth) / 100).toInt()
                val right = (((centerX - width / 2) * photoWidth) / 100).toInt()
                val top = (((centerY - height / 2) * photoHeight) / 100).toInt()
                val bottom = (((centerY + height / 2) * photoHeight) / 100).toInt()

                val rect = Rect(left, top, right, bottom)
                canvas.drawRect(rect, paintBackground)

                // text
                val x = rect.centerX() - (paintText.measureText(age.toString()) / 2)
                val y = rect.centerY() - ((paintText.descent() + paintText.ascent()) / 2)
                canvas.drawText(age.toString(), x, y, paintText)
            }
        }

        return bitmap
    }

    fun getGender(resources: Resources): Bitmap {
        val photoWidth = photo.width
        val photoHeight = photo.height

        val paintTransparent = Paint(Paint.ANTI_ALIAS_FLAG)
        paintTransparent.color = Color.TRANSPARENT

        val bitmap = getBitmap()

        GlobalScope.launch {
            val canvas = getCanvas(bitmap)

            for (face in faces) {
                if (face.attributes.gender != null) {
                    var genderSign =
                        if (face.attributes.gender.value == ConstValues.GENDER_FEMALE) BitmapFactory.decodeResource(resources, R.drawable.gender_female
                        ) else BitmapFactory.decodeResource(resources, R.drawable.gender_male)

                    val width = face.width.toFloat()
                    val height = face.height.toFloat() / 2

                    val centerX = face.center.x.toFloat()
                    val centerY = face.center.y.toFloat() + (1.6 * height)

                    // transparent helper
                    val left = (((centerX + width / 2) * photoWidth) / 100).toInt()
                    val right = (((centerX - width / 2) * photoWidth) / 100).toInt()
                    val top = (((centerY - height / 2) * photoHeight) / 100).toInt()
                    val bottom = (((centerY + height / 2) * photoHeight) / 100).toInt()

                    // we want to draw helper and gender sign below detected face, not exactly on it
                    val rect = Rect(left, top, right, bottom)
                    canvas.drawRect(rect, paintTransparent)

                    // gender sign
                    val x = rect.exactCenterX() + (rect.width() / 4)
                    val y = rect.exactCenterY() - (rect.height() / 2)

                    val scale = rect.height()
                    genderSign = genderSign.scale(scale, scale, true)
                    canvas.drawBitmap(genderSign, x, y, null)
                }

            }
        }

        return bitmap
    }

    fun isGenderInfoAvailable(): Boolean {
        var result = false

        for (face in faces) {
            if (face.attributes.gender != null) {
                if (face.attributes.gender.value != null) result = true
            }
        }

        return result
    }

    private fun getBitmap() = Bitmap.createBitmap(photo.width, photo.height, Bitmap.Config.ARGB_8888)

    private suspend fun getCanvas(bitmap: Bitmap): Canvas {
        val url = URL(photo.url)

        val canvas = Canvas(bitmap)

        val photoBitmap = getImageFromURL(url)
        canvas.drawBitmap(photoBitmap, Matrix(), null)

        return canvas
    }

    private suspend fun getImageFromURL(url: URL): Bitmap {
        val bitmap = GlobalScope.async {
            BitmapFactory.decodeStream(withContext(Dispatchers.IO) {
                url.openConnection().getInputStream()
            })
        }
        return bitmap.await()
    }

}