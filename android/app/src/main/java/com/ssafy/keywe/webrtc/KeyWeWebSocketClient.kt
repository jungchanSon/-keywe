package com.ssafy.keywe.webrtc

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.ssafy.keywe.core.di.module.WebSocketClientQualifier
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.webrtc.data.AcceptMessage
import com.ssafy.keywe.webrtc.data.RequestMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.moshi.withMoshi
import org.hildan.krossbow.stomp.frame.FrameBody
import org.hildan.krossbow.stomp.frame.StompFrame
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import timber.log.Timber
import javax.inject.Inject


class SignalWebSocketListener @Inject constructor() : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Timber.d("WebSocket onMessage: $text")

    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
    }

}

class KeyWeWebSocket @Inject constructor(
    private val webSocketListener: SignalWebSocketListener,
    @WebSocketClientQualifier private val okHttpClient: OkHttpClient,
    private val tokenManager: TokenManager,
) {
    //    private val webSocket: WebSocket
    private var stompClient: StompClient
    private var session: StompSession? = null

    val requestFactory = PolymorphicJsonAdapterFactory.of(RequestMessage::class.java, "type")
//        .withSubtype(Plane::class.java, VehicleType.PLANE.name)

    private val moshi: Moshi =
        Moshi.Builder().add(requestFactory).addLast(KotlinJsonAdapterFactory()).build()

    init {
//        val signalRequest = Request.Builder().url(socketUrl).build()
//        webSocket = okHttpClient.newWebSocket(signalRequest, webSocketListener)

//        val okHttpClient = OkHttpClient.Builder().callTimeout(1, TimeUnit.MINUTES)
//            .pingInterval(10, TimeUnit.SECONDS).build()
        val wsClient = OkHttpWebSocketClient(okHttpClient)
        stompClient = StompClient(wsClient)
    }

    suspend fun connect(): Boolean {
        val token = tokenManager.getToken()
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
//        val signalMessage: Flow<StompFrame.Message> =

        Log.d("subscribe", "${SubScribeEndPoint + profileId}")
        return session!!.subscribe(
            StompSubscribeHeaders(
                destination = SubScribeEndPoint + profileId,
            )
        )
//        signalMessage.collect {
//            val message = moshi.adapter(WebSocketMessage::class.java).fromJson(it.bodyAsText)
//        }
    }

    suspend fun sendRequest(storeId: String) {
        val requestMessage = RequestMessage(storeId)
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