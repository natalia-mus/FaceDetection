package com.example.facedetection.api

interface RepositoryCallback<T> {
    fun onSuccess(data: T)
    fun onError()
}