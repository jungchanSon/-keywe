package com.ssafy.keywe.data.websocket

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.ssafy.keywe.webrtc.KeyWeWebSocket
import com.ssafy.keywe.webrtc.data.WebSocketMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SignalType {
    CONNECT, SUBSCRIBE, REQUEST, ACCEPT, CLOSE,
}

@AndroidEntryPoint
class SignalService : Service() {
    @Inject
    lateinit var keyWeWebSocket: KeyWeWebSocket
    private val serviceJob = SupervisorJob()
    private val scope = CoroutineScope(serviceJob)
    private val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    // Service에서 발생하는 WebSocket 메시지를 외부로 노출하기 위한 Flow
    private val _stompMessageFlow = MutableSharedFlow<WebSocketMessage>()
    val stompMessageFlow: Flow<WebSocketMessage> get() = _stompMessageFlow

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when (intent.action) {
                SignalType.CONNECT.toString() -> {
                    scope.launch {
                        keyWeWebSocket.connect()
                    }
                }

                SignalType.SUBSCRIBE.toString() -> {
                    val profileId = intent.getStringExtra("profileId")!!
                    Log.d("SignalService", "profileId: $profileId")

                    scope.launch {
//                        val message: Flow<StompFrame.Message> = keyWeWebSocket.subscribe(profileId)
//                        keyWeWebSocket.sub
//
//                        message.collect {
//                            val message =
//                                moshi.adapter(WebSocketMessage::class.java).fromJson(it.bodyAsText)
//                        }

                        // keyWeWebSocket.subscribe()로 받은 Flow를 collect하고,
                        // JSON 파싱 후 _stompMessageFlow에 emit
                        keyWeWebSocket.subscribe(profileId).collect { frame ->
                            val message = moshi.adapter(WebSocketMessage::class.java)
                                .fromJson(frame.bodyAsText)
//                            val intent = Intent("custom-event-name")
//                            intent.putExtra("data", "전달할 데이터")
//                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

                            Log.d("SignalService", "Received message: $message")
//                            message?.let { _stompMessageFlow.emit(it) }
                        }
                    }
                }

                SignalType.REQUEST.toString() -> {
                    scope.launch {
                        keyWeWebSocket.sendRequest("\"Hello, World!\"")
                    }
                }

                SignalType.ACCEPT.toString() -> {
                    scope.launch {
                        keyWeWebSocket.sendAccept("\"Hello, World!\"")
                    }
                }

                SignalType.CLOSE.toString() -> {
                    scope.launch {
                        keyWeWebSocket.sendClose("\"Hello, World!\"")
                        stopSelf()
                    }
                }


            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}