package com.ssafy.keywe.webrtc.viewmodel

// SignalViewModel.kt

import androidx.lifecycle.ViewModel
import com.ssafy.keywe.webrtc.data.SignalRepository
import com.ssafy.keywe.webrtc.data.WebSocketMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> get() = _isConnected

    private val _isTimeOut = MutableStateFlow(false)
    val isTimeOut: StateFlow<Boolean> get() = _isTimeOut

    private val _errorMessageInConnecting = MutableStateFlow<String?>(null)
    val errorMessageInConnecting: StateFlow<String?> get() = _errorMessageInConnecting


    fun setConnected(value: Boolean) {
        _isConnected.value = value
    }
}
