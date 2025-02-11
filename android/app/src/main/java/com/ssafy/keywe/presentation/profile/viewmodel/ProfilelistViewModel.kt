package com.ssafy.keywe.presentation.profile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.ApiResponseHandler.onSuccess
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.data.dto.Status
import com.ssafy.keywe.data.dto.profile.ProfileData
import com.ssafy.keywe.domain.profile.GetAllProfileModel
import com.ssafy.keywe.domain.profile.ProfileRepository
import com.ssafy.keywe.viewmodel.AddMemberViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository, private val tokenManager: TokenManager
) : ViewModel() {
    private val _profilesUi = MutableStateFlow<List<ProfileData>>(emptyList())
    val profilesUi = _profilesUi.asStateFlow()

    private val _profiles = MutableStateFlow<List<GetAllProfileModel>>(emptyList())
    val profiles = _profiles.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllProfile().onSuccess(::handleSuccess)
        }
    }

    fun selectAccount(model: GetAllProfileModel) {
        val accessToken = _profiles.value.first {
            it.id == model.id
        }.id.toString()
        tokenManager.saveCacheAccessToken(accessToken)
        viewModelScope.launch {
            tokenManager.saveAccessToken(accessToken)
        }
    }

    fun addProfile(profile: ProfileData) {
        viewModelScope.launch {
            val currentProfiles = _profilesUi.value.toMutableList()
            currentProfiles.add(profile)
            _profilesUi.value = currentProfiles
        }
    }


//    fun addProfile(profile: ProfileData) {
//        _profiles.update { currentProfiles ->
//            currentProfiles + profile
//        }
//    }

    fun observeProfileAddedEvent(addMemberViewModel: AddMemberViewModel) {
        viewModelScope.launch {
            addMemberViewModel.profileAddedEvent.collect { newProfile ->
                addProfile(newProfile) // ✅ 프로필 추가
            }
        }
    }

    // 프로필 수정
    fun updateProfile(updatedProfile: ProfileData) {
        _profilesUi.update { currentList ->
            currentList.map {
                if (it.userId == updatedProfile.userId) updatedProfile else it
            }
        }
    }

    // 프로필 삭제
    fun deleteProfile(profileId: String) {
        _profilesUi.update { currentList ->
            currentList.filter { it.userId != profileId }
        }
    }


    private fun handleSuccess(
        model: List<GetAllProfileModel>,
    ) {
        Log.d("model", "model")
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



