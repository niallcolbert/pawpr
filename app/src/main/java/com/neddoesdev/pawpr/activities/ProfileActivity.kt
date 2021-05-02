package com.neddoesdev.pawpr.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_profile.*
import com.neddoesdev.pawpr.R
import com.neddoesdev.pawpr.main.MainApp
import com.neddoesdev.pawpr.models.ProfileModel
import org.jetbrains.anko.toast

class ProfileActivity : AppCompatActivity() {
    var profile = ProfileModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        app = application as MainApp

        btnAdd.setOnClickListener() {
            profile.name = profileName.text.toString()
            if (profile.name.isNotEmpty()) {
            }
            else {
                toast("Please enter a name")
            }
        }
    }
}