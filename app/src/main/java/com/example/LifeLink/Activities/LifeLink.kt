package com.example.LifeLink.Activities

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LifeLink: Application(){
    override fun onCreate() {
        super.onCreate()
        val notificationChannel= NotificationChannel(
            "LifeLink",
            "LifeLink",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}