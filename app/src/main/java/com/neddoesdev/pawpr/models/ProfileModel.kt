package com.neddoesdev.pawpr.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileModel (
        var profileImage: String = "",
        var name: String = "",
        var breed: String = "",
        var bio: String = "",
        var gender: String = "male",
        var isPuppy: Boolean = false,
        var isFixed: Boolean = false,
        var lat : Double = 0.0,
        var lng: Double = 0.0,
        var zoom: Float = 0f
    ) : Parcelable


@Parcelize
data class Location(
        var lat: Double = 0.0,
        var lng: Double = 0.0,
        var zoom: Float = 0f
    ) : Parcelable