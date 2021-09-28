package com.example.facedetection.model.datamodel

import com.example.facedetection.model.datamodel.facesinfo.Center

data class ProcessedImage(val center: Center, val width: Float, val height: Float, val age: Int)