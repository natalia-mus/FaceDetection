package com.example.facedetection.model.datamodel.apiusage

data class APIUsageData(
    val limit: Int,
    val remaining: Int,
    val used: Int,
    val percentageUsage: Int
)
