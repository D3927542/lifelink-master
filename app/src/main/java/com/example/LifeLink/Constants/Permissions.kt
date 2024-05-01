package com.example.LifeLink.Constants

import android.Manifest

class Permissions {
    companion object{
        val allPermissions = arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.SEND_SMS,
            Manifest.permission.SET_ALARM,
            Manifest.permission.POST_NOTIFICATIONS
        )
    }
}