package com.neddoesdev.pawpr.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class ProfileModel (
        var uid: String? = "",
        var userId: String = "",
        var profileImage: String = "",
        var name: String = "",
        var breed: String = "",
        var bio: String = "",
        var gender: String = "male",
//        var isPuppy: String = "",
//        var isFixed: String = "",
        var lat : Double = 0.0,
        var lng: Double = 0.0,
        var zoom: Float = 0f
    ) : Parcelable
{


    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
                "uid" to uid,
                "userId" to userId,
                "profileImage" to profileImage,
                "name" to name,
                "breed" to breed,
                "bio" to bio,
                "gender" to gender,
//                "isPuppy" to isPuppy,
//                "isFixed" to isFixed,
                "lat" to lat,
                "lng" to lng,
                "zoom" to zoom
        )
    }
}

@Parcelize
data class Location(
        var lat: Double = 0.0,
        var lng: Double = 0.0,
        var zoom: Float = 0f
    ) : Parcelable