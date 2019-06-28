package com.kaigarrott.stopwatchkt

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

class StopwatchService : Service() {
private var notification: Notification? = null

        override fun onBind(intent: Intent): IBinder? {
        return null
        }

        override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (notification == null) notification = setupNotification()
        startForeground(123, notification)
        return START_STICKY
        }

private fun setupNotification(): Notification {

        val builder: Notification.Builder
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        val channel = NotificationChannel(CHANNEL_ID, "Stopwatch",
        NotificationManager.IMPORTANCE_HIGH)
        channel.description = "Live stopwatch times"
        manager.createNotificationChannel(channel)
        builder = Notification.Builder(applicationContext, CHANNEL_ID)
        } else {
        builder = Notification.Builder(applicationContext)
        }
        return builder.setContentTitle("Stopwatch")
        .setOngoing(true)
        .build()
        }

        companion object {

private val CHANNEL_ID = "stopwatch_channel"
        }
        }
