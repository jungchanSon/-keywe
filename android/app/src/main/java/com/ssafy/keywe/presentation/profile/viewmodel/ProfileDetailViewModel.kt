package com.ssafy.keywe.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.common.manager.ProfileIdManager
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.profile.GetProfileDetailResponse
//import com.ssafy.keywe.data.dto.profile.GetProfileRequest
import com.ssafy.keywe.domain.profile.GetProfileDetailModel
import com.ssafy.keywe.domain.profile.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _profileData = MutableStateFlow<GetProfileDetailResponse?>(null)
    val profiledatailData = _profileData.asStateFlow()

    private val _state = MutableStateFlow<GetProfileDetailModel?>(null)
    val state = _state.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        loadProfileDetail()
    }

    fun loadProfileDetail() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            ProfileIdManager.profileId.value?.let { profileId ->
                when (val result = repository.getProfileDetail(profileId)) {
                    is ResponseResult.Success -> {
                        _state.value = result.data
                    }

                    is ResponseResult.ServerError -> {
                        _error.value = "서버 오류가 발생했습니다"
                    }

                    is ResponseResult.Exception -> {
                        _error.value = "네트워크 오류가 발생했습니다"
                    }
                }
            }
            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}
