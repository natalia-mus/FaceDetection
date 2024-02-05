package com.example.facedetection

import android.content.Context
import android.content.SharedPreferences

object Settings {

    private const val SETTINGS = "settings"
    private const val API_KEY = "api_key"

    private var instance: SharedPreferences? = null

    fun initSettings(context: Context) {
        if (instance == null) {
            instance = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        }
    }

    fun isAPIKeySet() = getAPIKey().isNotEmpty()

    fun saveAPIKey(newAPIKey: String) {
        if (instance != null) {
            instance!!.edit().putString(API_KEY, newAPIKey).apply()
        }
    }

    fun getAPIKey(): String {
        var result = ""

        if (instance != null) {
            val apiKey = instance!!.getString(API_KEY, "")

            if (apiKey is String) {
                result = apiKey
            }
        }
        return result
    }
}