package com.example.facedetection.model

import android.content.res.Resources
import android.graphics.*
import androidx.core.graphics.scale
import com.example.facedetection.R
import com.example.facedetection.model.datamodel.facesinfo.Photo
import com.example.facedetection.util.ConstValues
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
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

    suspend fun detectFaces(): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f


        val result = GlobalScope.async {
            val bitmap = getBitmap()
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

                val rect = getRect(centerX, centerY, width, height)
                canvas.drawRect(rect, paint)
            }

            return@async bitmap
        }

        return result.await()
    }

    suspend fun estimateAge(): Bitmap {
        val paintText = Paint(Paint.ANTI_ALIAS_FLAG)
        paintText.color = Color.BLACK

        val paintBackground = Paint(Paint.ANTI_ALIAS_FLAG)
        paintBackground.color = Color.WHITE
        paintBackground.alpha = 170


        val result = GlobalScope.async {
            val bitmap = getBitmap()
            val canvas = getCanvas(bitmap)

            for (face in faces) {
                val age = face.attributes.ageEst.value.toInt()

                val width = face.width.toFloat()
                val height = face.height.toFloat() / 2

                val centerX = face.center.x.toFloat()
                val centerY = face.center.y.toFloat() + (1.6 * height).toFloat()

                // rectangle
                val rect = getRect(centerX, centerY, width, height)
                canvas.drawRect(rect, paintBackground)

                // text
                val x = rect.centerX() - (paintText.measureText(age.toString()) / 2)
                val y = rect.centerY() - ((paintText.descent() + paintText.ascent()) / 2)
                paintText.textSize = height * 3
                canvas.drawText(age.toString(), x, y, paintText)
            }

            return@async bitmap
        }

        return result.await()
    }

    suspend fun getGender(resources: Resources): Bitmap {
        val paintTransparent = Paint(Paint.ANTI_ALIAS_FLAG)
        paintTransparent.color = Color.TRANSPARENT


        val result = GlobalScope.async {
            val bitmap = getBitmap()
            val canvas = getCanvas(bitmap)

            for (face in faces) {
                if (face.attributes.gender != null) {
                    var genderSign =
                        if (face.attributes.gender.value == ConstValues.GENDER_FEMALE) BitmapFactory.decodeResource(
                            resources, R.drawable.gender_female
                        ) else BitmapFactory.decodeResource(resources, R.drawable.gender_male)

                    val width = face.width.toFloat()
                    val height = face.height.toFloat() / 2

                    val centerX = face.center.x.toFloat()
                    val centerY = face.center.y.toFloat() + (1.6 * height).toFloat()

                    // transparent helper
                    val rect = getRect(centerX, centerY, width, height)
                    canvas.drawRect(rect, paintTransparent)

                    // gender sign
                    val x = rect.exactCenterX() + (rect.width() / 4)
                    val y = rect.exactCenterY() - (rect.height() / 2)

                    val scale = rect.height()
                    genderSign = genderSign.scale(scale, scale, true)
                    canvas.drawBitmap(genderSign, x, y, null)
                }
            }

            return@async bitmap
        }

        return result.await()
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

    private fun getRect(centerX: Float, centerY: Float, width: Float, height: Float): Rect {
        val photoWidth = photo.width
        val photoHeight = photo.height

        val left = (((centerX + width / 2) * photoWidth) / 100).toInt()
        val right = (((centerX - width / 2) * photoWidth) / 100).toInt()
        val top = (((centerY - height / 2) * photoHeight) / 100).toInt()
        val bottom = (((centerY + height / 2) * photoHeight) / 100).toInt()

        return Rect(left, top, right, bottom)
    }

}