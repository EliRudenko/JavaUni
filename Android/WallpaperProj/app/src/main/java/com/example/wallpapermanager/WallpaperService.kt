package com.example.wallpapermanager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat

class WallpaperService : Service() {

    private val handler = Handler(Looper.getMainLooper())


    private val wallpaperNames = arrayOf("wall1", "wall2", "wall3")
    private var index = 0

    private val task = object : Runnable {
        override fun run() {
            tryChangeWallpaper()
            handler.postDelayed(this, 10_000)
        }
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(1, createNotification())

        handler.post(task)
    }


    override fun onDestroy() {
        handler.removeCallbacks(task)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun tryChangeWallpaper() {
        try {
            val name = wallpaperNames[index]
            val resId = resources.getIdentifier(name, "drawable", packageName)

            if (resId == 0) {
                Log.e(TAG, "Drawable not found: $name. Put $name.jpg/png into res/drawable")
                index = (index + 1) % wallpaperNames.size
                return
            }

            val bmp = BitmapFactory.decodeResource(resources, resId)
            if (bmp == null) {
                Log.e(TAG, "Bitmap decode failed for: $name")
                index = (index + 1) % wallpaperNames.size
                return
            }

            WallpaperManager.getInstance(this).setBitmap(bmp)
            Log.d(TAG, "Wallpaper set: $name")

            index = (index + 1) % wallpaperNames.size
        } catch (e: Exception) {
            Log.e(TAG, "Wallpaper change error", e)
        }
    }

    private fun createNotification(): Notification {
        val manager = getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Wallpaper changer",
                NotificationManager.IMPORTANCE_LOW
            )
            manager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_gallery)
            .setContentTitle("Wallpaper changer")
            .setContentText("Changing wallpaper every 10 seconds")
            .setOngoing(true)
            .build()
    }

    private companion object {
        const val TAG = "WallpaperService"
        const val CHANNEL_ID = "wallpaper_channel"
        const val NOTIF_ID = 1
    }
}
