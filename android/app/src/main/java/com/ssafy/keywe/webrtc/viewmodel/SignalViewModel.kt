package com.ssafy.keywe.webrtc.viewmodel

// SignalViewModel.kt

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.webrtc.data.SignalRepository
import com.ssafy.keywe.webrtc.data.WebSocketMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignalViewModel @Inject constructor(
    private val signalRepository: SignalRepository,
) : ViewModel() {
    // Service에서 업데이트한 최신 메시지를 UI에 전달
    val stompMessageFlow: StateFlow<WebSocketMessage?> = signalRepository.stompMessageFlow
    val connected: StateFlow<Boolean> = signalRepository.connected
    val subscribed: StateFlow<Boolean> = signalRepository.subscribed

    private val _seconds = MutableStateFlow(20)
    val seconds: StateFlow<Int> get() = _seconds

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> get() = _isConnected

    private val _isTimeOut = MutableStateFlow(false)
    val isTimeOut: StateFlow<Boolean> get() = _isTimeOut

    private val _errorMessageInConnecting = MutableStateFlow<String?>(null)
    val errorMessageInConnecting: StateFlow<String?> get() = _errorMessageInConnecting

    init {
        startCountdown()
    }

    private fun startCountdown() {
        viewModelScope.launch {
            while (_seconds.value > 0) {
                delay(1000)
                _seconds.value -= 1
            }
        }
    }

    fun setConnected(value: Boolean) {
        _isConnected.value = value
    }
}
