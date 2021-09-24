package com.example.facedetection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.facedetection.api.Repository
import com.example.facedetection.api.RepositoryCallback
import com.example.facedetection.model.FacesInfo

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getFromAPI()
    }

    private fun getFromAPI() {
        Repository.getFacesInfo("url", object : RepositoryCallback<FacesInfo> {
            override fun onSuccess(data: FacesInfo) {}

            override fun onError() {}
        })
    }
}