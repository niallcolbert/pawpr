package com.neddoesdev.pawpr.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.neddoesdev.pawpr.R
import com.neddoesdev.pawpr.helpers.readImage
import com.neddoesdev.pawpr.helpers.showImagePicker
import com.neddoesdev.pawpr.main.MainApp
import com.neddoesdev.pawpr.models.Location
import com.neddoesdev.pawpr.models.ProfileModel
import kotlinx.android.synthetic.main.activity_profile.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast


class ProfileActivity : AppCompatActivity() {
    var profile = ProfileModel()
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    var location = Location(52.256, -7.104, 15f)
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

        chooseImage.setOnClickListener {
            chooseImage.setOnClickListener {
                showImagePicker(this, IMAGE_REQUEST)
            }
        }

        profileLocation.setOnClickListener {
            startActivityForResult (intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    profile.profileImage = data.getData().toString()
                    profileImage.setImageBitmap(readImage(this, resultCode, data))
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    location = data.extras?.getParcelable<Location>("location")!!
                }
            }
        }
    }
}