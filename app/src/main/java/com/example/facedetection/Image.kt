package com.example.facedetection

object Image {

    private var image: ByteArray? = null

    fun getImage(): ByteArray? {
        return image
    }

    fun setImage(image: ByteArray) {
        this.image = image
    }
}