package com.ssafy.keywe.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.dto.profile.ProfileData
import com.ssafy.keywe.viewmodel.AddMemberViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _profiles = MutableStateFlow<List<ProfileData>>(emptyList())
    val profiles = _profiles.asStateFlow()

    fun addProfile(profile: ProfileData) {
        _profiles.update { currentProfiles ->
            currentProfiles + profile
        }
    }

    fun observeProfileAddedEvent(addMemberViewModel: AddMemberViewModel) {
        viewModelScope.launch {
            addMemberViewModel.profileAddedEvent.collect { newProfile ->
                addProfile(newProfile) // ✅ 프로필 추가
            }
        }
    }

}

