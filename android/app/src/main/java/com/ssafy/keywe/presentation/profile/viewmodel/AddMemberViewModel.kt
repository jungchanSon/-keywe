package com.ssafy.keywe.viewmodel

//import com.ssafy.keywe.presentation.profile.state.AddMemberState
//import com.ssafy.keywe.presentation.profile.state.VerificationStatus
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.state.AddMemberState
import com.ssafy.keywe.data.state.VerificationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMemberViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(AddMemberState())
    val state = _state.asStateFlow()

    fun onNameChange(name: String) {
        _state.update { it.copy(name = name) }
    }

    fun onTabSelect(tab: Int) {
        _state.update { it.copy(selectedTab = tab) }
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
                        3,
                        7
                    )
                }-${newNumbersOnly.substring(7)}"
            }

            // 커서 위치 계산
            val newCursorPosition = formatted.length.coerceAtMost(formatted.length)

            // 상태 업데이트
            _state.update {
                it.copy(
                    phone = formatted,
                    isPhoneValid = newNumbersOnly.length == 11
                )
            }

            // TextFieldValue 업데이트
            _phoneTextFieldValue.value = TextFieldValue(
                text = formatted,
                selection = TextRange(newCursorPosition)
            )
        }
    }

    fun onVerificationCodeChange(code: String) {
        if (code.length <= 6 && code.all { it.isDigit() }) {
            _state.update { it.copy(verificationCode = code) }
            if (code.length == 6) {
                verifyCode()
            }
        }
    }

    fun onSimplePasswordChange(password: String) {
        if (password.length <= 4 && password.all { it.isDigit() }) {
            _state.update { it.copy(simplePassword = password) }
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
            _state.update { it.copy(isVerificationSent = true) }
            // 인증번호 전송 API 호출
        }
    }

    fun verifyCode() {
        viewModelScope.launch {
            if (state.value.verificationCode.length == 6) {
                // 인증번호 검증 API 호출
                val isSuccess = state.value.verificationCode == "123456"  // 임시 검증 로직
                _state.update {
                    it.copy(
                        verificationStatus = if (isSuccess)
                            VerificationStatus.SUCCESS
                        else
                            VerificationStatus.FAILURE
                    )
                }
            }
        }
    }

    fun addMember() {
        viewModelScope.launch {
            try {
                // API 호출하여 멤버 추가
                // 성공 시 ProfileChoice 화면으로 이동
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

//// 프로필 목록 상태관리
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
