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
    HOME, SELECTAPP, PROFILE, PERMISSION, KIOSK
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenManager: TokenManager, // 로그인 상태 확인용
    private val profileDataStore: ProfileDataStore,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _splashRouteType = mutableStateOf(SplashRouteType.SELECTAPP)
    val splashRouteType: State<SplashRouteType> = _splashRouteType

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            runBlocking {
                val token = tokenManager.getToken()
                val storeId = tokenManager.getStoreId()
                val isFirstJoin = profileDataStore.isFirstJoinFlow.map { isJoinFirst ->
                    isJoinFirst.takeIf {
                        it != null
                    }
                }.first()

                if (isFirstJoin == null) {
                    _splashRouteType.value = SplashRouteType.PERMISSION
                } else if (token == null) {
                    _splashRouteType.value = SplashRouteType.SELECTAPP
                } else {
                    if (storeId != null) {
                        _splashRouteType.value = SplashRouteType.KIOSK
                        tokenManager.isKiosk = true
                    } else if (JWTUtil.isTempToken(token.split(" ")[1])) {
                        _splashRouteType.value = SplashRouteType.PROFILE
                        tokenManager.isKiosk = false
                    } else {
                        _splashRouteType.value = SplashRouteType.HOME
                        tokenManager.isKiosk = false
                    }
                }
                val profileId = profileDataStore.profileIdFlow.map { profileId ->
                    profileId.takeIf {
                        it != null
                    }
                }.first()
                Log.d("profileID", "profileID = $profileId")
                if (profileId != null) ProfileIdManager.updateProfileId(profileId)

                _isLoading.value = false
            }
        }
    }
}
