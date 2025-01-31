package com.ssafy.keywe.presentation.splash.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.util.JWTUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

enum class SplashRouteType {
    HOME, LOGIN, PROFILE
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenManager: TokenManager, // 로그인 상태 확인용
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _splashRouteType = mutableStateOf(SplashRouteType.LOGIN)
    val splashRouteType: State<SplashRouteType> = _splashRouteType

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            runBlocking {
                val token = tokenManager.getToken()
                token?.let {
                    if (JWTUtil.isTempToken(it.split(" ")[1])) {
                        _splashRouteType.value = SplashRouteType.PROFILE
                    } else {
                        _splashRouteType.value = SplashRouteType.HOME
                    }
                }
                _isLoading.value = false
            }
        }
    }
}