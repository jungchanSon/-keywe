package com.ssafy.keywe.presentation.profile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.PushNotificationManager
import com.ssafy.keywe.data.ApiResponseHandler.onException
import com.ssafy.keywe.data.ApiResponseHandler.onServerError
import com.ssafy.keywe.data.ApiResponseHandler.onSuccess
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.TokenManager
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
) : ViewModel() {
    private val _profilesUi =
        MutableStateFlow<List<GetProfileListModel>>(emptyList()) //프로필 목록을 보여주는 상태관리
    val profilesUi = _profilesUi.asStateFlow()

    private val _profiles = MutableStateFlow<List<GetProfileListModel>>(emptyList())
    val profiles = _profiles.asStateFlow()

    init {
        getProfileList()
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

    fun selectAccount(model: GetProfileListModel) {
        Log.d("PROFILE ID", "selected ProfileId = ${model.id}")
        viewModelScope.launch {
            val request = SelectProfileRequest(
                model.id.toLong()
            )
            authRepository.selectProfile(request).onSuccess(::saveToken).onException { e, message ->
                Log.d("Select User onException", "SelectError $message")
            }.onServerError { status ->
                Log.d("Select User onServerError", "SelectError $status")
            }

        }

    }

    private fun saveToken(model: SelectProfileModel) {
        val token = PushNotificationManager.token.value
        val deviceId = PushNotificationManager.deviceId.value
        val request = FCMRequest(token!!, deviceId!!)
        tokenManager.saveCacheAccessToken(model.accessToken)
        viewModelScope.launch {
            tokenManager.saveAccessToken(model.accessToken)
            fcmRepository.registFCM(request)
//            tokenManager.saveRefreshToken(model.refreshToken)
        }

    }

//    private fun postProfile(profile: GetProfileRequest) {
//        viewModelScope.launch {
//            val currentProfiles = _profilesUi.value.toMutableList()
//            currentProfiles.add(profile)
//            _profilesUi.value = currentProfiles
//        }
//    }


//    fun addProfile(profile: ProfileData) {
//        _profiles.update { currentProfiles ->
//            currentProfiles + profile
//        }
//    }

//    fun observeProfileAddedEvent(addMemberViewModel: AddMemberViewModel) {
//        viewModelScope.launch {
//            addMemberViewModel.profileAddedEvent.collect { newProfile ->
//                postProfile(newProfile) // ✅ 프로필 추가
//            }
//        }
//    }

//    // 프로필 수정
//    fun updateProfile(updatedProfile: UpdateProfileRequest) {
//        _profilesUi.update { currentList ->
//            currentList.map {
//                if (it.name == updatedProfile.name) updatedProfile else it
//            }
//        }
//    }

//    // 프로필 삭제
//    fun deleteProfile(profileId: String) {
//        _profilesUi.update { currentList ->
//            currentList.filter { it.phone != profileId }
//        }
//    }


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



