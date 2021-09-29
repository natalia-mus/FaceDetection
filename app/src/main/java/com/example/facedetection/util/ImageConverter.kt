package com.example.facedetection.util

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

object ImageConverter {

    fun convertToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val output = outputStream.toByteArray()
        val image = Base64.encodeToString(output, Base64.DEFAULT)

        return image
    }
}