package com.ssafy.keywe.presentation.splash.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.common.manager.ProfileIdManager
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.data.datastore.ProfileDataStore
import com.ssafy.keywe.util.JWTUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

enum class SplashRouteType {
    HOME, LOGIN, PROFILE
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenManager: TokenManager, // 로그인 상태 확인용
    private val profileDataStore: ProfileDataStore,
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
                Log.d("Token Route", "token = $token")
                if (token == null) {
                    _splashRouteType.value = SplashRouteType.LOGIN
                } else {
                    if (JWTUtil.isTempToken(token.split(" ")[1])) {
                        _splashRouteType.value = SplashRouteType.PROFILE
                    } else {
                        _splashRouteType.value = SplashRouteType.HOME
                    }
                }
                val profileId = profileDataStore.profileIdFlow.map { profileId ->
                    profileId.takeIf {
                        it != null
                    }
                }.first()

                if (profileId != null) ProfileIdManager.updateProfileId(profileId)

                _isLoading.value = false
            }
        }
    }
}