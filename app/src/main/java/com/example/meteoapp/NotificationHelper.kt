package com.example.meteoapp

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat


class NotificationHelper(private val context: String) {

    private val channelId = "weather_channel_id"
    private val notificationId = 1234
    private val NOTIFICATION_PERMISSION_CODE = 1001

/*
 init {

       createNotificationChannel()
   }
 private fun createNotificationChannel() {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           val name = "Weather Channel"
           val descriptionText = "Weather notifications"
           val importance = NotificationManager.IMPORTANCE_DEFAULT
           val channel = NotificationChannel(channelId, name, importance).apply {
               description = descriptionText
           }

           val notificationManager =
               context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
           notificationManager.createNotificationChannel(channel)
       }
   }

   fun showWeatherNotification(weatherMessage: String) {
       if (ContextCompat.checkSelfPermission(
               context,
               Manifest.permission.ACCESS_NOTIFICATION_POLICY
           ) == PackageManager.PERMISSION_GRANTED
       ) {
           val builder = NotificationCompat.Builder(context, channelId)
               .setSmallIcon(R.drawable.ic_notifications)
               .setContentTitle("Weather Update")
               .setContentText(weatherMessage)
               .setPriority(NotificationCompat.PRIORITY_DEFAULT)

           val notificationManager = NotificationManagerCompat.from(context)
           notificationManager.notify(notificationId, builder.build())
       } else {
          ActivityCompat.requestPermissions(
              context as Activity,
              arrayOf(Manifest.permission.ACCESS_NOTIFICATION_POLICY),
              NOTIFICATION_PERMISSION_CODE
          )
       }
   }*/


}
