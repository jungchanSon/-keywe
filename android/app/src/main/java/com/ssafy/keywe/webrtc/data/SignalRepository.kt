package com.ssafy.keywe.webrtc.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignalRepository @Inject constructor() {
    // 최신 메시지를 보관 (처음에는 null)
    private val _stompMessageFlow = MutableStateFlow<WebSocketMessage?>(null)
    val stompMessageFlow: StateFlow<WebSocketMessage?> = _stompMessageFlow

    private val _connected = MutableStateFlow<Boolean>(false)
    val connected: StateFlow<Boolean> = _connected

    private val _subscribed = MutableStateFlow<Boolean>(false)
    val subscribed: StateFlow<Boolean> = _subscribed

    // 외부에서 메시지를 업데이트할 수 있도록 함수 제공
    fun updateMessage(message: WebSocketMessage) {
        _stompMessageFlow.value = message
    }

    // 외부에서 메시지를 업데이트할 수 있도록 함수 제공
    fun updateConnect(connect: Boolean) {
        _connected.value = connect
    }

    // 외부에서 메시지를 업데이트할 수 있도록 함수 제공
    fun updateSubscribed(subscribed: Boolean) {
        _subscribed.value = subscribed
    }

}
