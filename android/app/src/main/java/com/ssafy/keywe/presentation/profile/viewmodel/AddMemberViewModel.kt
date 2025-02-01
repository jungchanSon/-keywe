package com.ssafy.keywe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
<<<<<<< HEAD:android/app/src/main/java/com/ssafy/keywe/viewmodel/AddMemberViewModel.kt
import com.ssafy.keywe.data.state.AddMemberState
import com.ssafy.keywe.data.state.VerificationStatus
=======
import com.ssafy.keywe.presentation.profile.state.AddMemberState
import com.ssafy.keywe.presentation.profile.state.VerificationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
>>>>>>> 15bb131 (profile screen):android/app/src/main/java/com/ssafy/keywe/presentation/profile/viewmodel/AddMemberViewModel.kt
import kotlinx.coroutines.delay
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

    fun onPhoneChange(input: String) {
        // 현재 상태의 전화번호에서 숫자만 추출
        val oldNumbersOnly = _state.value.phone.filter { it.isDigit() }
        // 새 입력값에서 숫자만 추출
        val newNumbersOnly = input.filter { it.isDigit() }

        // 숫자만 추출
//        val numbersOnly = input.filter { it.isDigit() }

        // 11자리 제한
        if (newNumbersOnly.length <= 11) {
            // 하이픈 추가 포맷팅
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

            // 새로운 커서 위치 계싼
            val newCusorPosition = when {
                //숫자가 추가된 경우
                newNumbersOnly.length > oldNumbersOnly.length -> {
                    when {
                        newNumbersOnly.length <= 3 -> newNumbersOnly.length
                        newNumbersOnly.length <= 7 -> newNumbersOnly.length + 1
                        else -> newNumbersOnly.length + 2
                    }
                }
                // 숫자가 삭제된 경우
                else -> {
                    when {
                        newNumbersOnly.length <= 3 -> newNumbersOnly.length
                        newNumbersOnly.length <= 7 -> newNumbersOnly.length + 1
                        else -> newNumbersOnly.length + 2
                    }

                }
            }

            _state.update {
                it.copy(
                    phone = formatted,
                    isPhoneValid = newNumbersOnly.length == 11,
                    phoneSelection = newCusorPosition
                )
            }
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

    fun sendVerification() {
        viewModelScope.launch {
            delay(1000) // 임시 API 응답 시뮬레이션
            _state.update { it.copy(isVerificationSent = true) }
        }
    }

    fun verifyCode() {
        viewModelScope.launch {
            delay(1000) // 임시 검증 로직
            val isSuccess = _state.value.verificationCode == "123456"
            _state.update {
                it.copy(
                    verificationStatus = if (isSuccess)
                        VerificationStatus.SUCCESS else VerificationStatus.FAILURE,
                    isVerificationValid = isSuccess
                )
            }
        }
    }


    fun addMember() {
        viewModelScope.launch {
            // 멤버 추가 로직 구현
        }
    }
}
