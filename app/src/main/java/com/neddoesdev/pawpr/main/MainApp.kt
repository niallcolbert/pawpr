package com.neddoesdev.pawpr.main

import android.app.Application
import com.google.firebase.auth.FirebaseAuth

class MainApp : Application() {

    // [START declare_auth]
    lateinit var auth: FirebaseAuth
    // [END declare_auth]

    override fun onCreate() {
        super.onCreate()

    }
}