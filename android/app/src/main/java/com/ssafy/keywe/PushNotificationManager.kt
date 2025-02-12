package com.ssafy.keywe

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object PushNotificationManager {
    private var countReceived: Int = 0
    val _token = MutableStateFlow<String?>(null)
    var token = _token.asStateFlow()

    fun setDataReceived(count: Int) {
        this.countReceived = count
    }

    fun getDataReceived(): Int {
        return this.countReceived
    }

    fun updateToken(token: String) {
        this._token.update {
            token
        }
    }
}