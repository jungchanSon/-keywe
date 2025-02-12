// SignalService.kt
package com.ssafy.keywe.data.websocket

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.ssafy.keywe.webrtc.KeyWeWebSocket
import com.ssafy.keywe.webrtc.data.STOMPTYPE
import com.ssafy.keywe.webrtc.data.SignalRepository
import com.ssafy.keywe.webrtc.data.WebSocketMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SignalType {
    CONNECT, SUBSCRIBE, REQUEST, ACCEPT, CLOSE,
}

enum class SignalState {
    CONNECTING, CONNECTED, DISCONNECTED
}

@AndroidEntryPoint
class SignalService : Service() {
    @Inject
    lateinit var keyWeWebSocket: KeyWeWebSocket

    // Repository를 주입받아서 메시지 업데이트에 사용
    @Inject
    lateinit var signalRepository: SignalRepository

    private val serviceJob = SupervisorJob()
    private val scope = CoroutineScope(serviceJob)
    private val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                SignalType.CONNECT.toString() -> {
                    scope.launch {
                        when (keyWeWebSocket.connect()) {
                            true -> Log.d("SignalService", "WebSocket connected")
                            false -> Log.d("SignalService", "WebSocket connection failed")
                        }
                    }
                }

                SignalType.SUBSCRIBE.toString() -> {
                    val profileId = it.getStringExtra("profileId")!!
                    Log.d("SignalService", "profileId: $profileId")
                    scope.launch {
                        // keyWeWebSocket.subscribe()로 받은 Flow를 collect하고, JSON 파싱 후 처리
                        keyWeWebSocket.subscribe(profileId).collect { frame ->
                            val message = moshi.adapter(WebSocketMessage::class.java)
                                .fromJson(frame.bodyAsText)
                            Log.d("SignalService", "Received message: $message")
                            message?.let { msg ->
                                handleSTOMP(msg)
                            }
                        }
                    }
                }

                SignalType.REQUEST.toString() -> {
                    val storeId = it.getStringExtra("storeId")!!
                    scope.launch {
                        keyWeWebSocket.sendRequest(storeId)
                    }
                }

                SignalType.ACCEPT.toString() -> {
                    val sessionId = it.getStringExtra("sessionId")!!
                    scope.launch {
                        keyWeWebSocket.sendAccept(sessionId)
                    }
                }

                SignalType.CLOSE.toString() -> {
                    val sessionId = it.getStringExtra("sessionId")!!
                    scope.launch {
                        keyWeWebSocket.sendClose(sessionId)
                        stopSelf()
                    }
                }

                else -> {}
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        scope.cancel()
        Log.d("SignalService", "onDestroy")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // STOMP 메시지 처리 및 Repository 업데이트
    private fun handleSTOMP(stomp: WebSocketMessage) {
        when (stomp.type) {
            STOMPTYPE.REQUESTED -> Log.d("SignalService", "REQUESTED ${stomp.data}")
            STOMPTYPE.WAITING -> Log.d("SignalService", "WAITING ${stomp.data}")
            STOMPTYPE.ACCEPTED -> Log.d("SignalService", "ACCEPTED")
            STOMPTYPE.TIMEOUT -> Log.d("SignalService", "TIMEOUT")
            STOMPTYPE.END -> Log.d("SignalService", "END")
            STOMPTYPE.ERROR -> Log.d("SignalService", "ERROR")
        }
        // Repository에 메시지 업데이트 (UI에서 구독 가능)
        signalRepository.updateMessage(stomp)
    }
}
