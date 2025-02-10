package com.ssafy.keywe.viewmodel

//import com.ssafy.keywe.presentation.profile.state.AddMemberState
//import com.ssafy.keywe.presentation.profile.state.VerificationStatus
import android.net.Uri
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.R
import com.ssafy.keywe.data.dto.profile.ProfileData
import com.ssafy.keywe.data.state.AddMemberState
import com.ssafy.keywe.data.state.VerificationStatus
import com.ssafy.keywe.presentation.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMemberViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(AddMemberState())
    val state = _state.asStateFlow()

    // 프로필 추가 이벤트를 위한 SharedFlow
    private val _profileAddedEvent = MutableSharedFlow<ProfileData>()
    val profileAddedEvent = _profileAddedEvent.asSharedFlow()

    // 이미지 처리
    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri = _profileImageUri.asStateFlow()

    // 인증번호 입력
    private val _verificationMessage = MutableStateFlow<String>("")
    val verificationMessage = _verificationMessage.asStateFlow()

    private val _isVerificationButtonEnabled = MutableStateFlow(false)
    val isVerificationButtonEnabled = _isVerificationButtonEnabled.asStateFlow()

    // 추가하기 버튼 상태관리
    private val _isAddButtonEnabled = MutableStateFlow(false)
    val isAddButtonEnabled = _isAddButtonEnabled.asStateFlow()

    // 버튼 활성화 상태를 체크하는 함수
    private fun checkAddButtonEnabled() {
        val currentState = state.value
        _isAddButtonEnabled.value = if (currentState.selectedTab == 0) {
            // 부모님일 경우
            currentState.name.isNotEmpty() && currentState.isPhoneValid && currentState.verificationStatus == VerificationStatus.SUCCESS && currentState.simplePassword.length == 4
        } else {
            // 자녀일 경우
            currentState.name.isNotEmpty()
        }
    }


    fun onNameChange(name: String) {
        _state.update { it.copy(name = name) }
        checkAddButtonEnabled()
    }

    fun onTabSelect(tab: Int) {
        _state.update { it.copy(selectedTab = tab) }
        checkAddButtonEnabled()
    }


    // ViewModel 내부에 MutableStateFlow로 선언
    private val _phoneTextFieldValue = MutableStateFlow(TextFieldValue(""))
    val phoneTextFieldValue = _phoneTextFieldValue.asStateFlow()

    fun onPhoneChange(input: TextFieldValue) {
        // 숫자만 추출
        val newNumbersOnly = input.text.filter { it.isDigit() }

        // 11자리로 제한
        if (newNumbersOnly.length <= 11) {
            // 하이픈 포맷팅
            val formatted = when {
                newNumbersOnly.length <= 3 -> newNumbersOnly
                newNumbersOnly.length <= 7 -> "${newNumbersOnly.take(3)}-${
                    newNumbersOnly.substring(
                        3
                    )
                }"

                else -> "${newNumbersOnly.take(3)}-${
                    newNumbersOnly.substring(
                        3, 7
                    )
                }-${newNumbersOnly.substring(7)}"
            }

            // 커서 위치 계산
            val newCursorPosition = formatted.length.coerceAtMost(formatted.length)

            // 상태 업데이트
            _state.update {
                it.copy(
                    phone = formatted, isPhoneValid = newNumbersOnly.length == 11
                )
            }

            // TextFieldValue 업데이트
            _phoneTextFieldValue.value = TextFieldValue(
                text = formatted, selection = TextRange(newCursorPosition)
            )
        }
        checkAddButtonEnabled()
    }

    fun onVerificationCodeChange(code: String) {
        if (code.length <= 6 && code.all { it.isDigit() }) {
            _state.update { it.copy(verificationCode = code) }
            _isVerificationButtonEnabled.value = code.length == 6
        }
    }

    fun onSimplePasswordChange(password: String) {
        if (password.length <= 4 && password.all { it.isDigit() }) {
            _state.update { it.copy(simplePassword = password) }
            checkAddButtonEnabled()
        }
    }

    //    fun sendVerification() {
//        viewModelScope.launch {
//            if (state.value.isPhoneValid) {
//                // 인증번호 전송 로직
//                _state.update {
//                    it.copy(
//                        isVerificationSent = true,
//                        verificationStatus = VerificationStatus.NONE
//                    )
//                }
//            }
//        }
//    }
    fun sendVerification() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isVerificationSent = true, verificationStatus = VerificationStatus.NONE
                )
            }
            // 인증번호 전송 API 호출
            _verificationMessage.value = "인증번호가 전송되었습니다."
        }
    }

    fun verifyCode() {
        viewModelScope.launch {
            if (state.value.verificationCode.length == 6) {
                // 인증번호 검증 API 호출
                val isSuccess = state.value.verificationCode == "123456"  // 임시 검증 로직
                _state.update {
                    it.copy(
                        verificationStatus = if (isSuccess) VerificationStatus.SUCCESS
                        else VerificationStatus.FAILURE
                    )
                }
                _verificationMessage.value = if (isSuccess) {
                    "인증이 성공되었습니다."
                } else {
                    "인증이 실패되었습니다."
                }
                checkAddButtonEnabled()
            }
        }
    }

    fun updateProfileImage(uri: Uri) {
        _profileImageUri.value = uri
    }

    fun addMember(viewModel: ProfileViewModel) {
        viewModelScope.launch {
            try {
                val defaultProfileImage = R.drawable.humanimage
                val newProfile = ProfileData(
                    userId = "temp_id",
                    name = state.value.name,
                    phone = state.value.phone,
                    profileImage = profileImageUri.value?.toString(),
                    role = if (state.value.selectedTab == 0) "PARENT" else "CHILD",
                    simplePassword = state.value.simplePassword
                )
                viewModel.addProfile(newProfile)


//                 이벤트 발생
//                _profileAddedEvent.emit(newProfile)

                // 상태 초기화
                _state.update {
                    it.copy(
                        name = "",
                        phone = "",
                        verificationCode = "",
                        simplePassword = "",
                        selectedTab = 0
                    )
                }

            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }
}
//// `프로필 목록 상태관리
//@HiltViewModel
//class ProfileViewModel @Inject constructor() : ViewModel() {
//    private val _profiles = MutableStateFlow<List<Profile>>(emptyList())
//    val profiles = _profiles.asStateFlow()
//
//    fun addProfile(profile: Profile) {
//        _profiles.update { currentList ->
//            currentList + profile
//        }
//    }
//}
//
