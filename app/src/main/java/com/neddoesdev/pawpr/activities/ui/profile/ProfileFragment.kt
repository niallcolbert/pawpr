package com.neddoesdev.pawpr.activities.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.neddoesdev.pawpr.R
import com.neddoesdev.pawpr.activities.MapsActivity
import com.neddoesdev.pawpr.helpers.showImagePicker
import com.neddoesdev.pawpr.main.MainApp
import com.neddoesdev.pawpr.models.Location
import com.neddoesdev.pawpr.models.ProfileModel
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast


class ProfileFragment : Fragment() {
    var profile = ProfileModel()
    lateinit var app: MainApp
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    var location = Location(52.256, -7.104, 15f)
    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_profile, container, false)
        root.btnAdd.setOnClickListener() {
            activity?.title = getString(R.string.profile)

            val userId = app.auth.currentUser!!.uid

            profile.name = profileName.text.toString()
            profile.bio = profileBio.text.toString()
            profile.breed = profileBreed.text.toString()
//            profile.isPuppy = puppyToggle.isChecked().toString()
//            profile.isFixed = fixedToggle.isChecked().toString()
            profile.lng = location.lng
            profile.lat = location.lat
            profile.zoom = location.zoom
            profile.userId = userId

            var id: Int = radioGroup.checkedRadioButtonId
            if (id!=-1){
                profile.gender = if (id == R.id.radio_male) "male" else "female"
            }

            if (profile.name.isNotEmpty()) {
                saveProfile(profile)
            } else {
                toast("Please enter a name")
            }
        }

        root.chooseImage.setOnClickListener {
            chooseImage.setOnClickListener {
                showImagePicker(this, IMAGE_REQUEST)
            }
        }

        root.profileLocation.setOnClickListener {
            startActivityForResult (intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
        }

        getUserProfile()

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
    }

    fun getUserProfile() {
        val userId = app.auth.currentUser!!.uid

        app.database.child("profile").child(userId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        toast("error : ${error.message}")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val children = snapshot.children
                        children.forEach {
                            if (it.hasChild("breed")) {
                                val dbprofile = it.getValue<ProfileModel>(ProfileModel::class.java)
                                if (dbprofile!!.userId == app.auth.currentUser!!.uid ) {
                                    profile.uid = dbprofile.uid
                                    profileName.setText(dbprofile.name)
                                    profileBio.setText(dbprofile.bio)
                                    profileBreed.setText(dbprofile.breed)
//                                    puppyToggle.setChecked(dbprofile.isPuppy == "true")
//                                    fixedToggle.setChecked(dbprofile.isFixed == "true")
                                    location.lat = dbprofile.lat
                                    location.lng = dbprofile.lng
                                    location.zoom = dbprofile.zoom
                                    var radio_id = if (dbprofile.gender == "male") R.id.radio_male else R.id.radio_female
                                    radioGroup.check(radio_id)
                                    return
                                }
                            }
                        }

                        app.database.child("profile").child(profile.userId.toString())
                                .removeEventListener(this)
                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    profile.profileImage = data.getData().toString()
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    location = data.extras?.getParcelable<Location>("location")!!
                }
            }
        }
    }

    fun saveProfile(profileData: ProfileModel) {
        val uid = app.auth.currentUser!!.uid
        val key: String
        if (profile.uid.toString().isEmpty()) {
            key = app.database.child("profile").push().key.toString()
            if (key == null) {
                return
            }
            profileData.uid = key
        } else {
            key = profile.uid.toString()
            profileData.uid = profile.uid
        }

        val values = profileData.toMap()

        val childUpdates = HashMap<String, Any>()
        childUpdates["/profile/$uid/$key"] = values

        app.database.updateChildren(childUpdates)
        toast("Your profile was updated")
    }

}