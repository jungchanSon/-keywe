package com.ssafy.keywe.presentation.profile.viewmodel

//import com.ssafy.keywe.data.dto.profile.GetProfileRequest
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.common.manager.ProfileIdManager
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.profile.GetProfileDetailResponse
import com.ssafy.keywe.data.dto.profile.UpdateProfileRequest
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
    val profiledetailData = _profileData.asStateFlow()

    private val _state = MutableStateFlow<GetProfileDetailModel?>(null)
    val state = _state.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    //GetProfileDetailModel 외 정보들을 EditMemberState에 가져오기
    private val _password = MutableStateFlow<String?>("")
    val password = _password.asStateFlow()
    private val _isModified = MutableStateFlow(false)
    val isModified = _isModified.asStateFlow() // 현재 프로필 수정이 이루어졌는지 추적하는 것


    init {
        loadProfileDetail()
    }


    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
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

    fun updateProfile(context: Context, profileViewModel: ProfileViewModel, imageUri: Uri?) {
        viewModelScope.launch {
            val currentState = state.value ?: return@launch  // state가 null이면 즉시 종료, 없어도 되긴할거각ㅌㅇ
            val passwordValue = password.value

            val request = if (currentState.role == "PARENT") {
                UpdateProfileRequest(
                    name = currentState.name,
                    phone = currentState.phone ?: "".replace("-", ""),
                    password = passwordValue
                )
            } else {
                UpdateProfileRequest(
                    name = currentState.name
                )
            }
//                name = currentState.name ?: "",
//                phone = currentState.phone ?: "".replace("-", ""),
//                password = currentState.password
//            )

            when (val result = repository.updateProfile(request, context, imageUri)) {
                is ResponseResult.Success -> {
                    Log.d("EditProfile", "프로필 수정 성공")
                    _isModified.value = false // 상태 업데이트

                    profileViewModel.refreshProfileList()
                    loadProfileDetail()

                }

                is ResponseResult.ServerError -> {
                    Log.e("EditProfile", "서버 오류 발생")
                }

                is ResponseResult.Exception -> {
                    Log.e("EditProfile", "네트워크 오류 발생: ${result.message}")
                }
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
