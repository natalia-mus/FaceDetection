package com.example.facedetection.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
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
    private lateinit var apiKeyField: EditText
    private lateinit var imageExpirationCheckbox: CheckBox
    private lateinit var loadingSection: ConstraintLayout

    private lateinit var viewModel: ChangeAPIKeyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_api_key)

        viewModel = ViewModelProvider(this).get(ChangeAPIKeyViewModel::class.java)

        setView()
        setListeners()
        setObservers()

        viewModel.getAPISettings()
    }

    private fun setView() {
        buttonGoToWebsite = findViewById(R.id.activity_change_api_key_go_to_website)
        buttonChangeAPIKey = findViewById(R.id.activity_change_api_key_button_change)
        apiKeyField = findViewById(R.id.activity_change_api_key_api_key)
        imageExpirationCheckbox = findViewById(R.id.activity_change_api_key_image_expiration_checkbox)
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
    }

    private fun setObservers() {
        viewModel.loading.observe(this) { loadingStatusChanged(it) }
        viewModel.apiKey.observe(this) { setAPIKeyFieldText(it) }
        viewModel.imageExpirationValue.observe(this) { setImageExpirationCheckBoxValue(it) }
        viewModel.isAPIKeyValid.observe(this) { handleAPIKeyConfirmationStatus(it) }
    }

    private fun setAPIKeyFieldText(apiKey: String) {
        apiKeyField.setText(apiKey)
    }

    private fun setImageExpirationCheckBoxValue(checked: Boolean) {
        imageExpirationCheckbox.isChecked = checked
    }

    private fun changeAPIKey() {
        val newAPIKey = apiKeyField.text.toString()
        val setImageExpirationTime = imageExpirationCheckbox.isChecked
        saveAPIKey(newAPIKey, setImageExpirationTime)
    }

    private fun saveAPIKey(apiKey: String, setImageExpirationTime: Boolean) {
        viewModel.saveChanges(apiKey, setImageExpirationTime)
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
            APIKeyConfirmationStatus.Empty -> showToast(resources.getString(R.string.api_key_empty))
            APIKeyConfirmationStatus.Invalid -> showToast(resources.getString(R.string.api_key_invalid))
            APIKeyConfirmationStatus.Error -> showToast(resources.getString(R.string.api_key_error))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

}