package com.musicplayer.android

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkConfiguration

class MyApplication:Application() {
    private lateinit var appOpenManager: MainActivity.MyAppOpenManager
    companion object{
        const val CHANNEL_ID="channel1"
        const val PLAY="play"
        const val NEXT="next"
        const val PREVIOUS="previous"
        const val EXIT="exit"

    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Now playing song",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.description = "this is important song"
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
        //app lovin

        AppLovinSdk.getInstance(this).mediationProvider = "max"
        AppLovinSdk.getInstance(this).initializeSdk({ configuration: AppLovinSdkConfiguration -> appOpenManager = MainActivity.MyAppOpenManager(applicationContext) })
        //   AppLovinSdk.getInstance(this).showMediationDebugger()

    }
}