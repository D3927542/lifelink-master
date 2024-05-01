package com.example.LifeLink.Service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
class MyAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Check if the received intent is the alarm intent
        if (intent?.action == "MY_UNIQUE_ACTION") {
            // Execute your desired method here
            // For example:
            // performBackgroundTask()
            val notificationService = context?.let { NotificationService(it) }
            if (notificationService != null) {
                notificationService.showBasicNotification()
            }
        }
    }
}