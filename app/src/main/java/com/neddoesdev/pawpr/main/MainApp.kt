package com.neddoesdev.pawpr.main

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class MainApp : Application() {

    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference

    override fun onCreate() {
        super.onCreate()

    }
}