package com.example.facedetection.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.facedetection.R
import com.example.facedetection.Settings

class ChangeAPIKeyActivity : AppCompatActivity() {

    companion object {
        const val URI = "https://imgbb.com/login"
    }

    private lateinit var buttonGoToWebsite: Button
    private lateinit var buttonChangeAPIKey: Button
    private lateinit var buttonContinueWithDefaultKey: Button
    private lateinit var apiKey: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_api_key)

        setView()
        setListeners()
    }

    private fun setView() {
        buttonGoToWebsite = findViewById(R.id.activity_change_api_key_go_to_website)
        buttonChangeAPIKey = findViewById(R.id.activity_change_api_key_button_change)
        buttonContinueWithDefaultKey = findViewById(R.id.activity_change_api_key_continue)
        apiKey = findViewById(R.id.activity_change_api_key_api_key)
    }

    private fun setListeners() {
        buttonGoToWebsite.setOnClickListener() {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(URI))
            startActivity(intent)
        }

        buttonChangeAPIKey.setOnClickListener() {
            changeAPIKey()
        }

        buttonContinueWithDefaultKey.setOnClickListener() {
            continueWithDefaultKey()
        }
    }

    private fun changeAPIKey() {
        val newAPIKey = apiKey.text.toString()
        saveAPIKey(newAPIKey)
        finish()
    }

    private fun continueWithDefaultKey() {
        saveAPIKey("")
        finish()
    }

    private fun saveAPIKey(apiKey: String) {
        val sharedPreferences = getSharedPreferences(Settings.SETTINGS, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(Settings.API_KEY, apiKey).apply()
    }

}