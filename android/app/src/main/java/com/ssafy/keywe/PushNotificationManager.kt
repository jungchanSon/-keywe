package com.ssafy.keywe

import kotlinx.coroutines.delay

object PushNotificationManager {
    private var countReceived: Int = 0

    fun setDataReceived(count: Int) {
        this.countReceived = count
    }

    fun getDataReceived(): Int {
        return this.countReceived
    }

    suspend fun registerTokenOnServer(token: String) {
        delay(2000)
    }

}