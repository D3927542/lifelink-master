package com.example.LifeLink.Service

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.example.LifeLink.Activities.SplashActivity
import com.example.LifeLink.R
import java.util.Calendar
import kotlin.random.Random

class NotificationService(
    private val context: Context
){
    private val notificationManager=context.getSystemService(NotificationManager::class.java)
    fun showBasicNotification(){
        notificationManager.activeNotifications.let {
            if(it.size > 0)
                return
        }
        // define the action button
        val intent = Intent(context, SplashActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_MUTABLE)

        val notification=NotificationCompat.Builder(context,"LifeLink")
            .setContentTitle("Wellness Checkup")
            .setChannelId("LifeLink")
            .setContentText("Everything good?")
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("If not press need help. Help will arrive soon...."))
            .addAction(NotificationCompat.Action(
                    R.drawable.logo,
                    "Need Help",
                    pendingIntent
                )
            )
            .build()

        notificationManager.notify(
            Random.nextInt(),
            notification
        )
    }
    fun scheduleAlarm(context: Context) { // FIXME:  
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, MyAlarmReceiver::class.java).apply {
            action = "MY_UNIQUE_ACTION"
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent,
            PendingIntent.FLAG_IMMUTABLE)

        // Set the alarm to start at 30 minutes intervals
        val intervalMillis = /*30 * 60 10* */1000  // 30 minutes in milliseconds
        val triggerTime = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.MILLISECOND, intervalMillis)
        }

        // Set repeating alarm that triggers every 30 minutes
        alarmMgr.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime.timeInMillis,
            intervalMillis.toLong(),
            pendingIntent
        )
    }
    private fun Context.bitmapFromResource(
        @DrawableRes resId:Int
    )= BitmapFactory.decodeResource(
        resources,
        resId
    )
}