package com.ssafy.keywe.presentation.profile.viewmodel

import android.net.Uri
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.dto.profile.ProfileData
import com.ssafy.keywe.presentation.profile.state.EditMemberState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMemberViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(EditMemberState())
    val state = _state.asStateFlow()

    // 휴대폰 번호 입력을 위한 TextField 상태
    private val _phoneTextFieldValue = MutableStateFlow(TextFieldValue(""))
    val phoneTextFieldValue = _phoneTextFieldValue.asStateFlow()


    // 이미지 처리를 위한 상태 추가
    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri = _profileImageUri.asStateFlow()

    fun updateProfileImage(uri: Uri) {
        _profileImageUri.value = uri
        _state.update {
            it.copy(isModified = true)
        }
    }


    fun onNameChange(name: String) {
        _state.update {
            it.copy(
                name = name,
                isModified = true
            )
        }
    }


    fun onPhoneChange(input: TextFieldValue) {
        val numbersOnly = input.text.filter { it.isDigit() }

        if (numbersOnly.length <= 11) {
            val formatted = when {
                numbersOnly.length <= 3 -> numbersOnly
                numbersOnly.length <= 7 -> "${numbersOnly.take(3)}-${numbersOnly.substring(3)}"
                else -> "${numbersOnly.take(3)}-${
                    numbersOnly.substring(
                        3,
                        7
                    )
                }-${numbersOnly.substring(7)}"
            }
            // 커서 위치 계산
            val newCursorPosition = formatted.length.coerceAtMost(formatted.length)

            _state.update {
                it.copy(
                    phone = formatted,
                    isModified = true
                )
            }
            // TextFieldValue 업데이트
            _phoneTextFieldValue.value = TextFieldValue(
                text = formatted,
                selection = TextRange(newCursorPosition)
            )
        }
    }

    fun onSimplePasswordChange(password: String) {
        if (password.length <= 4 && password.all { it.isDigit() }) {
            _state.update {
                it.copy(
                    simplePassword = password,
                    isModified = true
                )
            }
        }
    }

    fun deleteProfile() {
        viewModelScope.launch {
            try {
                // API 호출하여 프로필 업데이트
                // 성공 시 네비게이션 처리
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }


    fun updateProfile(profileViewModel: ProfileViewModel) {
        viewModelScope.launch {
            try {
                val updatedProfile = ProfileData(
                    userId = state.value.profileId,
                    name = state.value.name,
                    phone = state.value.phone,
                    profileImage = profileImageUri.value?.toString(),
                    role = state.value.role,
                    simplePassword = state.value.simplePassword
                )
                profileViewModel.updateProfile(updatedProfile)
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }

    fun loadMemberData(profileId: String) {
        viewModelScope.launch {
            // 기존 회원 정보 로드 로직 구현
            _state.update {
                it.copy(
                    profileId = profileId
                    // 다른 데이터 로드
                )
            }
        }
    }
}
