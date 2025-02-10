package com.ssafy.keywe.data.webrtc

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.Surface
import androidx.core.app.NotificationCompat
import com.ssafy.keywe.R

class MediaService : Service() {

    private var mediaProjection: MediaProjection? = null
    private lateinit var mediaProjectionManager: MediaProjectionManager
    private var virtualDisplay: VirtualDisplay? = null
    private var surface: Surface? = null

    override fun onCreate() {
        Log.d("MediaService", "onCreate called")
        super.onCreate()
        mediaProjectionManager =
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MediaService", "onStartCommand called")

        // 포그라운드 알람 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                1, createNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION
            )
        } else {
            startForeground(1, createNotification())
        }

        val mediaProjectionCallback = object : MediaProjection.Callback() {
            override fun onStop() {
                super.onStop()
                Log.d("TAG", "onStop: stopped screen casting permission")
                // screen capture stopped
            }
        }


        val screenSharingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("DATA", Intent::class.java)
        } else {
            intent?.getParcelableExtra<Intent>("DATA")
        }

//        val videoCapturer = ScreenCapturerAndroid(screenSharingIntent, mediaProjectionCallback)

        val resultCode = intent?.getIntExtra("RESULT_CODE", Activity.RESULT_CANCELED)
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("DATA", Intent::class.java)
        } else {
            intent?.getParcelableExtra<Intent>("DATA")
            TODO("VERSION.SDK_INT < TIRAMISU")
        }
        surface = intent?.getParcelableExtra("SURFACE")

        if (resultCode == Activity.RESULT_OK && data != null && surface != null) {
            startMediaProjection(resultCode, data)
        } else {
            stopSelf()  // 권한 없으면 종료
        }

        return START_STICKY
    }

    private fun startMediaProjection(resultCode: Int, data: Intent) {
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data)

        // 반드시 MediaProjection.Callback을 먼저 등록
        mediaProjection!!.registerCallback(object : MediaProjection.Callback() {
            override fun onStop() {
                super.onStop()
                stopSelf()
            }
        }, Handler(Looper.getMainLooper()))

        createVirtualDisplay()
    }

    private fun createVirtualDisplay() {
        val displayMetrics = resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        val density = displayMetrics.densityDpi

        virtualDisplay = mediaProjection?.createVirtualDisplay(
            "ScreenCapture",
            width,
            height,
            density,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
            surface,
            null,
            null
        )
    }


    private fun createNotification(): Notification {
        val channelId = "screen_capture_channel"
        val channelName = "Screen Capture Service"
        val notificationManager = getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId).setContentTitle("키오스크 대리 주문")
            .setContentText("현재 키오스크 대리 주문 중입니다.").setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }


    override fun onDestroy() {
        Log.d("MediaService", "onDestroy called")
        super.onDestroy()
        virtualDisplay?.release()
        mediaProjection?.stop()
    }

    override fun onBind(intent: Intent?): IBinder? = null


}
