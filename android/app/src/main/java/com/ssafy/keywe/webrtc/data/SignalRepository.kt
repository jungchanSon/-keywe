package com.ssafy.keywe.webrtc.data

import com.ssafy.keywe.webrtc.data.WebSocketMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignalRepository @Inject constructor() {
    // 최신 메시지를 보관 (처음에는 null)
    private val _stompMessageFlow = MutableStateFlow<WebSocketMessage?>(null)
    val stompMessageFlow: StateFlow<WebSocketMessage?> = _stompMessageFlow

    // 외부에서 메시지를 업데이트할 수 있도록 함수 제공
    fun updateMessage(message: WebSocketMessage) {
        _stompMessageFlow.value = message
    }
}
