package com.ssafy.keywe.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.ssafy.keywe.data.dto.profile.ProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    // 프로필 목록을 관리하는 StateFlow
    private val _profiles = MutableStateFlow<List<ProfileData>>(emptyList())
    val profiles = _profiles.asStateFlow()

    // 프로필 추가 함수
    fun addProfile(profile: ProfileData) {
        _profiles.update { currentList ->
            currentList + profile
        }
    }

    // 프로필 목록 초기화/업데이트 함수
    fun updateProfiles(newProfiles: List<ProfileData>) {
        _profiles.value = newProfiles
    }

    // 프로필 데이터 클래스
    data class ProfileData(
        val name: String,
        val imageUrl: String? = null
    )
}
