package com.ssafy.keywe.presentation.profile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafy.keywe.PushNotificationManager
import com.ssafy.keywe.R
import com.ssafy.keywe.common.manager.ProfileIdManager
import com.ssafy.keywe.data.ApiResponseHandler.onException
import com.ssafy.keywe.data.ApiResponseHandler.onServerError
import com.ssafy.keywe.data.ApiResponseHandler.onSuccess
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.data.datastore.ProfileDataStore
import com.ssafy.keywe.data.dto.Status
import com.ssafy.keywe.data.dto.auth.SelectProfileRequest
import com.ssafy.keywe.data.dto.fcm.FCMRequest
import com.ssafy.keywe.domain.auth.AuthRepository
import com.ssafy.keywe.domain.auth.SelectProfileModel
import com.ssafy.keywe.domain.fcm.FCMRepository
import com.ssafy.keywe.domain.profile.GetProfileListModel
import com.ssafy.keywe.domain.profile.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository, private val tokenManager: TokenManager,
    private val authRepository: AuthRepository,
    private val fcmRepository: FCMRepository,
    private val profileDataStore: ProfileDataStore,
) : ViewModel() {
    private val _profilesUi =
        MutableStateFlow<List<GetProfileListModel>>(emptyList()) //프로필 목록을 보여주는 상태관리
    val profilesUi = _profilesUi.asStateFlow()

    private val _profiles = MutableStateFlow<List<GetProfileListModel>>(emptyList())
    val profiles = _profiles.asStateFlow()

    init {
        viewModelScope.launch {
//            delay(200)
            getProfileList()
        }
    }

    private fun getProfileList() {
        viewModelScope.launch {
            when (val result = profileRepository.getProfileList()) {
                is ResponseResult.Success -> {
                    _profiles.value = result.data
                    Log.d("getProfileList", "성공함 결과값: ${result.data}")
                }

                is ResponseResult.ServerError -> {
                    println("서버 에러: ${result.status}")
                    Log.d("getProfileList", "서버 에러: ${result.status}")
                }

                is ResponseResult.Exception -> {
                    println("예외 발생: ${result.e.message}")
                    Log.d("getProfileList", "예외 발생: ${result.e.message}")
                }
            }
        }
    }

    suspend fun selectAccount(model: GetProfileListModel) {
        Log.d("PROFILE ID", "selected ProfileId = ${model.id}")
//        viewModelScope.launch {
        val request = SelectProfileRequest(
            model.id.toLong()
        )
        authRepository.selectProfile(request).onSuccess { response ->
            ProfileIdManager.updateProfileId(model.id.toLong())
            profileDataStore.saveProfileId(model.id.toLong())
            saveToken(response)
        }.onException { e, message ->
            Log.d("Select User onException", "SelectError $message")
        }.onServerError { status ->
            Log.d("Select User onServerError", "SelectError $status")
        }

//        }

    }


    fun refreshProfileList() {
        viewModelScope.launch {
            when (val result = profileRepository.getProfileList()) {
                is ResponseResult.Success -> {
                    val processedProfiles = result.data.map { profile ->
                        profile.copy(
                            image = profile.image?.takeIf { it.isNotBlank() }
                                ?: R.drawable.humanimage.toString() // 기본 이미지 적용
                        )
                    }
                    _profiles.value = processedProfiles
                    Log.d("refreshProfileList", "프로필 목록 새로고침 성공")
                }

                is ResponseResult.ServerError -> {
                    Log.e("refreshProfileList", "서버 에러: ${result.status}")
                }

                is ResponseResult.Exception -> {
                    Log.e("refreshProfileList", "예외 발생: ${result.e.message}")
                }
            }
        }
    }


    private fun saveToken(model: SelectProfileModel) {
        val token = PushNotificationManager.token.value
        val deviceId = PushNotificationManager.deviceId.value
        Log.d("ProfileViewModel", "token = $token")
        viewModelScope.launch {
            if (token == null) {
                Log.d("ProfileViewModel", "NoToken")
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.d(
                            "push notification device token", "failed with error: ${task.exception}"
                        )
                        return@OnCompleteListener
                    }
                    val token = task.result
                    Log.d("push notification device token", "token received: $token")
                    PushNotificationManager.updateToken(token)

                    val request = FCMRequest(token!!, deviceId!!)
                    tokenManager.saveCacheAccessToken(model.accessToken)
                    viewModelScope.launch {
                        tokenManager.saveAccessToken(model.accessToken)
                        fcmRepository.registFCM(request)
                    }
                })
            } else {
                val request = FCMRequest(token!!, deviceId!!)
                tokenManager.saveCacheAccessToken(model.accessToken)
                viewModelScope.launch {
                    tokenManager.saveAccessToken(model.accessToken)
                    fcmRepository.registFCM(request)
                }
            }


        }


    }


    private fun handleSuccess(
        model: List<GetProfileListModel>,
    ) {
        Log.d("model", "model = $model")
        _profiles.update {
            model
        }

    }

    private fun handleError(
        status: Status,
    ) {
//        _errorMessage.update {
//            "일치하는 사용자 정보가 없습니다."
//        }
    }

    private fun handleException(
        e: Throwable,
        errorMessage: String,
    ) {
//        _errorMessage.postValue(errorMessage)
    }
}



