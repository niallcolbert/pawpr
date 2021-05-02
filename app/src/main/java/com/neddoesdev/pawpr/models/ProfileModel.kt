package com.neddoesdev.pawpr.models

data class ProfileModel (
        var name: String = "",
        var breed: String = "",
        var bio: String = "",
        var gender: String = "",
        var isPuppy: Boolean = false,
        var isFixed: Boolean = false
    )