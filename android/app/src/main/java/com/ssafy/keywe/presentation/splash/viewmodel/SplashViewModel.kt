package com.ssafy.keywe.presentation.splash.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenManager: TokenManager, // 로그인 상태 확인용
) : ViewModel() {

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            runBlocking {
//                delay(2000) // 데이터를 로드하는 중임을 시뮬레이션 (네트워크 요청 가능)
//                _isLoggedIn.value = tokenManager.hasValidToken() // 토큰 상태 확인
//                _isLoading.value = false
            }

        }
    }
}