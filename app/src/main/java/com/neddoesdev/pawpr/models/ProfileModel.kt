package com.neddoesdev.pawpr.models

data class ProfileModel (
        var profileImage: String = "",
        var name: String = "",
        var breed: String = "",
        var bio: String = "",
        var gender: String = "male",
        var size: String = "s",
        var isPuppy: Boolean = false,
        var isFixed: Boolean = false
    )