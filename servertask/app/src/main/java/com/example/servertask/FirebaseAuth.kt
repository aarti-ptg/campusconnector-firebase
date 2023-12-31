package com.example.servertask

import android.app.Application
import com.google.firebase.FirebaseApp

class FirebaseAuth: Application(){

    override fun onCreate() {
            super.onCreate()
            // Initialize Firebase
            FirebaseApp.initializeApp(this)
    }


}