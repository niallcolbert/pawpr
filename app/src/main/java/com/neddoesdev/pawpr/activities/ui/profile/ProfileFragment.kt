package com.neddoesdev.pawpr.activities.ui.profile

import android.content.ContentResolver
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.neddoesdev.pawpr.R
import com.neddoesdev.pawpr.activities.MapsActivity
import com.neddoesdev.pawpr.helpers.readImage
import com.neddoesdev.pawpr.helpers.showImagePicker
import com.neddoesdev.pawpr.main.MainApp
import com.neddoesdev.pawpr.models.Location
import com.neddoesdev.pawpr.models.ProfileModel
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast


class ProfileFragment : Fragment() {
    var profile = ProfileModel()
    lateinit var app: MainApp
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    var location = Location(52.256, -7.104, 15f)
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp

//        btnAdd.setOnClickListener() {
//            profile.name = profileName.text.toString()
//            if (profile.name.isNotEmpty()) {
//            } else {
//                toast("Please enter a name")
//            }
//        }

//        chooseImage.setOnClickListener {
//            chooseImage.setOnClickListener {
//                showImagePicker(this, IMAGE_REQUEST)
//            }
//        }
//
//        profileLocation.setOnClickListener {
//            startActivityForResult (intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
//        }
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