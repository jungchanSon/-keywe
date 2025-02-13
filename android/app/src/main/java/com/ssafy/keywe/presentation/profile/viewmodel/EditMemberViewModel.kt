package com.ssafy.keywe.presentation.profile.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.common.manager.ProfileIdManager
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.datastore.ProfileDataStore
import com.ssafy.keywe.data.dto.profile.GetProfileRequest
import com.ssafy.keywe.data.dto.profile.UpdateProfileRequest
import com.ssafy.keywe.domain.profile.ProfileRepository
import com.ssafy.keywe.presentation.profile.state.EditMemberState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMemberViewModel @Inject constructor(
    private val repository: ProfileRepository, private val profileDataStore: ProfileDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(EditMemberState())
    val state = _state.asStateFlow()

    // 휴대폰 번호 입력을 위한 TextField 상태
    private val _phoneTextFieldValue = MutableStateFlow(TextFieldValue(""))
    val phoneTextFieldValue = _phoneTextFieldValue.asStateFlow()

    // 이미지 처리를 위한 상태 추가
    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri = _profileImageUri.asStateFlow()

//    private val _profileId = MutableStateFlow(-1L) // ✅ 선언 및 기본값 설정
//    val profileId: StateFlow<Long> = _profileId.asStateFlow() // ✅ 외부에서 읽을 수 있도록 제공

    init {
        viewModelScope.launch {
            // ✅ 앱 실행 시 저장된 profileId 불러오기
//            profileDataStore.profileIdFlow.collect { id ->
//                _profileId.value = id
//            }
        }
    }

    fun updateProfileImage(uri: Uri) {
        _profileImageUri.value = uri
        _state.update {
            it.copy(isModified = true)
        }
    }

    fun onNameChange(name: String) {
        _state.update {
            it.copy(
                name = name, isModified = true
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
                        3, 7
                    )
                }-${numbersOnly.substring(7)}"
            }
            // 커서 위치 계산
            val newCursorPosition = formatted.length.coerceAtMost(formatted.length)

            _state.update {
                it.copy(
                    phone = formatted, isModified = true
                )
            }
            // TextFieldValue 업데이트
            _phoneTextFieldValue.value = TextFieldValue(
                text = formatted, selection = TextRange(newCursorPosition)
            )
        }
    }

    fun onSimplePasswordChange(password: String) {
        if (password.length <= 4 && password.all { it.isDigit() }) {
            _state.update {
                it.copy(
                    password = password, isModified = true
                )
            }
        }
    }

    // 기존 데이터 로드
    fun loadProfile() {
        viewModelScope.launch {
//            val profileId = _profileId.value
//            if (profileId == null || profileId == -1L) {
//                Log.e("EditProfile", "프로필 ID가 없습니다.")
//                return@launch
//            } 프로필 아디이 사용할 경우
            ProfileIdManager.profileId.value?.let { profileId ->
                when (val result = repository.getProfileDetail(GetProfileRequest(profileId))) {
                    is ResponseResult.Success -> {
                        val profile = result.data
                        _state.update {
                            it.copy(
                                id = profile.id,
                                name = profile.name,
                                role = profile.role ?: "PARENT",
                                phone = profile.phone ?: "",
                                password = "", //profile.password ?:
                                isModified = false
                            )
                        }
                    }

                    is ResponseResult.ServerError -> {
                        Log.e("EditProfile", "서버 오류")
                    }

                    is ResponseResult.Exception -> {
                        Log.e("EditProfile", "네트워크 오류: ${result.message}")
                    }
                }
            }
        }
    }

    //수정 업데이트 정보 담기
    fun updateProfile(profileViewModel: ProfileViewModel) {
        viewModelScope.launch {

            val request = if (state.value.role == "PARENT") {
                val formattedPhone = state.value.phone.replace("-", "")
                UpdateProfileRequest(
                    name = state.value.name, phone = formattedPhone, password = state.value.password
                )
            } else {
                UpdateProfileRequest(
                    name = _state.value.name
                )
            }

            when (val result = repository.updateProfile(request)) {
                is ResponseResult.Success -> {
                    Log.d("EditProfile", "프로필 수정 성공")
                    _state.update { it.copy(isModified = false) }
                    profileViewModel.refreshProfileList()
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


    fun deleteProfile(profileId: Long) {
        viewModelScope.launch {
            when (val result = repository.deleteProfile(profileId)) {
                is ResponseResult.Success -> ProfileIdManager.clearProfileId()// 삭제 완료 처리
                else -> Log.e("EditProfile", "프로필 삭제 실패")// 에러 처리
            }
        }
    }

//    // API 연동
//    fun deleteProfile() {
//        viewModelScope.launch {
//            val profileId = ProfileIdManager.updateProfileId()
////            if (profileId == -1L) {
////                Log.e("EditProfile", "프로필 ID가 없습니다.")
////                return@launch
////            }
//
//            when (val result = repository.deleteProfile(profileId)) { // ✅ 삭제 시에만 profileId 사용
//                is ResponseResult.Success -> Log.d("EditProfile", "프로필 삭제 성공")
//                is ResponseResult.ServerError -> Log.e("EditProfile", "서버 오류 발생")
//                is ResponseResult.Exception -> Log.e("EditProfile", "네트워크 오류 발생: ${result.message}")
//            }
//        }
}


