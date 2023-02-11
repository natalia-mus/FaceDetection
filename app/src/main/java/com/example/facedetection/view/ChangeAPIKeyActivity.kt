package com.example.facedetection.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.facedetection.ErrorCode
import com.example.facedetection.R
import com.example.facedetection.Settings
import com.example.facedetection.api.RepositoryCallback
import com.example.facedetection.api.imagetourl.ImageToUrlRepository
import com.example.facedetection.model.datamodel.imagetourl.APIKeyConfirmationResult

class ChangeAPIKeyActivity : AppCompatActivity() {

    companion object {
        private const val URI = "https://imgbb.com/login"
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
        ImageToUrlRepository.confirmAPIKey(apiKey, object : RepositoryCallback<APIKeyConfirmationResult> {
            override fun onSuccess(data: APIKeyConfirmationResult) {
                if (data.error.code != ErrorCode.InvalidAPIKey.code) {
                    Settings.saveAPIKey(apiKey)
                } else {
                    // TODO
                }
            }

            override fun onError() {
                // TODO
            }
        })
    }

}