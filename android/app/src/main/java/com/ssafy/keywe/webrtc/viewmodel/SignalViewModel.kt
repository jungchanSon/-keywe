package com.ssafy.keywe.webrtc.viewmodel

// SignalViewModel.kt

import androidx.lifecycle.ViewModel
import com.ssafy.keywe.webrtc.data.SignalRepository
import com.ssafy.keywe.webrtc.data.WebSocketMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SignalViewModel @Inject constructor(
    private val signalRepository: SignalRepository,
) : ViewModel() {
    // Service에서 업데이트한 최신 메시지를 UI에 전달
    val stompMessageFlow: StateFlow<WebSocketMessage?> = signalRepository.stompMessageFlow
    val connected: StateFlow<Boolean> = signalRepository.connected
    val subscribed: StateFlow<Boolean> = signalRepository.subscribed
}
