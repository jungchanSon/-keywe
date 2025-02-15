package com.ssafy.keywe.webrtc

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.ssafy.keywe.core.di.module.WebSocketClientQualifier
import com.ssafy.keywe.webrtc.data.AcceptMessage
import com.ssafy.keywe.webrtc.data.RequestMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.moshi.withMoshi
import org.hildan.krossbow.stomp.frame.FrameBody
import org.hildan.krossbow.stomp.frame.StompFrame
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import javax.inject.Inject


class KeyWeWebSocket @Inject constructor(
    @WebSocketClientQualifier private val okHttpClient: OkHttpClient,
) {
    private var stompClient: StompClient
    private var session: StompSession? = null

    val requestFactory = PolymorphicJsonAdapterFactory.of(RequestMessage::class.java, "type")

    private val moshi: Moshi =
        Moshi.Builder().add(requestFactory).addLast(KotlinJsonAdapterFactory()).build()

    init {
        val wsClient = OkHttpWebSocketClient(okHttpClient)
        stompClient = StompClient(wsClient)
    }

    suspend fun connect(token: String): Boolean {
        Log.d("WebSocket", "connect $token")
        if (token != null) {
            session = token.run {
                stompClient.connect(
                    SocketUrl, customStompConnectHeaders = mapOf(
                        "Authorization" to token
                    )
                ).withMoshi(moshi)
            }
            return true
        } else {
            Log.e("WebSocket", "Token is null")
            return false
        }
    }

    suspend fun subscribe(profileId: String): Flow<StompFrame.Message> {
        Log.d("subscribe", "${SubScribeEndPoint + profileId}")
        return session!!.subscribe(
            StompSubscribeHeaders(
                destination = SubScribeEndPoint + profileId,
            )
        )
    }

    suspend fun sendRequest(storeId: String) {
        val requestMessage = RequestMessage("storeId")
        val json = Json.encodeToString(requestMessage)
        Log.d("sendRequest", "$json")
        val receipt = session!!.send(
            body = FrameBody.Text(json), headers = StompSendHeaders(
                destination = RequestEndPoint,
            )
        )
    }

    suspend fun sendAccept(sessionId: String) {
        val acceptMessage = AcceptMessage(sessionId)
        val json = Json.encodeToString(acceptMessage)
        Log.d("sendAccept", "$json")
        val receipt = session!!.send(
            body = FrameBody.Text(json), headers = StompSendHeaders(
                destination = AcceptEndPoint,
            )
        )

    }

    suspend fun close() {
        session!!.disconnect()
    }


    companion object {
        const val SocketUrl = "ws://i12a404.p.ssafy.io:8080/remote/ws"
        val SubScribeEndPoint = "/topic/"
        val RequestEndPoint = "/app/remote-order/request"
        val AcceptEndPoint = "/app/remote-order/accept"
    }
}