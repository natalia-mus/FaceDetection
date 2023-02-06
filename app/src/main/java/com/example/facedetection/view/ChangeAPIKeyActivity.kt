package com.example.facedetection.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.facedetection.R

class ChangeAPIKeyActivity : AppCompatActivity() {

    companion object{
        const val URI = "https://imgbb.com/login"
    }

    private lateinit var buttonGoToWebsite: Button
    private lateinit var buttonChangeAPIKey: Button
    private lateinit var buttonContinueWithDefaultKey: Button

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
    }

    private fun setListeners() {
        buttonGoToWebsite.setOnClickListener() {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(URI))
            startActivity(intent)
        }
    }
}