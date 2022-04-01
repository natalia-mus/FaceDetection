package com.example.facedetection.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.facedetection.R
import com.example.facedetection.model.datamodel.apiusage.APIUsageData

class APIUsageInfo(context: Context, private val apiUsage: APIUsageData) : Dialog(context) {

    private lateinit var percentageUsage: TextView
    private lateinit var usedValue: TextView
    private lateinit var limitValue: TextView
    private lateinit var remainingValue: TextView
    private lateinit var buttonClose: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.layout_api_usage)
        super.onCreate(savedInstanceState)

        setView()

        buttonClose.setOnClickListener() {
            onBackPressed()
        }
    }

    private fun setView() {
        percentageUsage = findViewById(R.id.api_usage_layout_percentage_usage_value)
        usedValue = findViewById(R.id.api_usage_layout_used_value)
        limitValue = findViewById(R.id.api_usage_layout_limit_value)
        remainingValue = findViewById(R.id.api_usage_layout_remaining_value)
        buttonClose = findViewById(R.id.api_usage_layout_button_close)

        percentageUsage.text = apiUsage.percentageUsage.toString()
        usedValue.text = apiUsage.used.toString()
        limitValue.text = apiUsage.limit.toString()
        remainingValue.text = apiUsage.remaining.toString()
    }

}