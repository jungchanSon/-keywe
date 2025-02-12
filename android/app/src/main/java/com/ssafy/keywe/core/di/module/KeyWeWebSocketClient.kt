package com.ssafy.keywe.core.di.module

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
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
) {
    private val webSocket: WebSocket
    private val socketUrl = "ws://i12a404.p.ssafy.io:8080"

    init {
        val signalRequest = Request.Builder().url(socketUrl).build()
        webSocket = okHttpClient.newWebSocket(signalRequest, webSocketListener)

    }

    fun sendMessage() {
        val jsonMessage = JSONObject().put("senderNickname", " message.senderNickname")
            .put("message", "message.message").put("date", "message.date").toString()
        webSocket.send(jsonMessage)
    }

    fun closeMessage() {
        webSocket.close(1000, "Connection Closed")
    }
}