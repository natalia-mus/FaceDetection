package com.example.facedetection

import android.content.Context
import android.content.SharedPreferences

object Settings {

    private const val SETTINGS = "settings"
    private const val API_KEY = "api_key"
    private const val EXPIRATION = "expiration"

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

    fun saveExpirationTime(expiration: Int) {
        if (instance != null) {
            instance!!.edit().putInt(EXPIRATION, expiration).apply()
        }
    }

    fun getAPIKey(): String {
        var result = ""

        if (instance != null) {
            val apiKey = instance!!.getString(API_KEY, "")

            if (apiKey != null) {
                result = apiKey
            }
        }
        return result
    }

    /**
     * Image expiration time in seconds
     */
    fun getImageExpirationTime(): Int {
        var result = 0

        if (instance != null) {
            result = instance!!.getInt(EXPIRATION, 0)
        }
        return result
    }
}