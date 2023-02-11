package com.example.facedetection.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.example.facedetection.R
import com.example.facedetection.viewmodel.APIKeyConfirmationStatus
import com.example.facedetection.viewmodel.ChangeAPIKeyViewModel

class ChangeAPIKeyActivity : AppCompatActivity() {

    companion object {
        private const val URI = "https://imgbb.com/login"
    }

    private lateinit var buttonGoToWebsite: Button
    private lateinit var buttonChangeAPIKey: Button
    private lateinit var buttonContinueWithDefaultKey: Button
    private lateinit var apiKey: EditText
    private lateinit var loadingSection: ConstraintLayout

    private lateinit var viewModel: ChangeAPIKeyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_api_key)

        viewModel = ViewModelProvider(this).get(ChangeAPIKeyViewModel::class.java)

        setView()
        setListeners()
        setObservers()
    }

    private fun setView() {
        buttonGoToWebsite = findViewById(R.id.activity_change_api_key_go_to_website)
        buttonChangeAPIKey = findViewById(R.id.activity_change_api_key_button_change)
        buttonContinueWithDefaultKey = findViewById(R.id.activity_change_api_key_continue)
        apiKey = findViewById(R.id.activity_change_api_key_api_key)
        loadingSection = findViewById(R.id.activity_change_api_key_loading_section)
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

    private fun setObservers() {
        viewModel.loading.observe(this) { loadingStatusChanged(it) }
        viewModel.isAPIKeyValid.observe(this) { handleAPIKeyConfirmationStatus(it) }
    }

    private fun changeAPIKey() {
        val newAPIKey = apiKey.text.toString()
        saveAPIKey(newAPIKey)
    }

    private fun continueWithDefaultKey() {
        saveAPIKey("")
    }

    private fun saveAPIKey(apiKey: String) {
        viewModel.saveAPIKey(apiKey)
    }

    private fun loadingStatusChanged(loading: Boolean) {
        if (loading) {
            loadingSection.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.GONE
        }
    }

    private fun handleAPIKeyConfirmationStatus(status: APIKeyConfirmationStatus) {
        when (status) {
            APIKeyConfirmationStatus.Valid -> {
                finish()
                showToast(resources.getString(R.string.api_key_valid))
            }
            APIKeyConfirmationStatus.Default -> {
                finish()
                showToast(resources.getString(R.string.api_key_default))
            }
            APIKeyConfirmationStatus.Invalid -> showToast(resources.getString(R.string.api_key_invalid))
            APIKeyConfirmationStatus.Error -> showToast(resources.getString(R.string.api_key_error))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

}