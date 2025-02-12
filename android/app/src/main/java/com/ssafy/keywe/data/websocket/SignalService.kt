package com.ssafy.keywe.data.websocket

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.ssafy.keywe.core.di.module.KeyWeWebSocket
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignalService : Service() {
    @Inject
    lateinit var keyWeWebSocket: KeyWeWebSocket

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        keyWeWebSocket.sendMessage()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        keyWeWebSocket.closeMessage()
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}