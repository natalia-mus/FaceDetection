package com.example.facedetection.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.facedetection.R
import com.example.facedetection.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url =
            "https://www.elleman.pl/uploads/media/default/0005/40/cec344175ad1976fe2a78e04a2843f97ce77270b.jpeg"

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.getFacesInfo(url)

    }

}