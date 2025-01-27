package com.ssafy.keywe.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.presentation.profile.state.AddMemberState
import com.ssafy.keywe.presentation.profile.state.VerificationStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddMemberViewModel : ViewModel() {
    private val _state = MutableStateFlow(AddMemberState())
    val state = _state.asStateFlow()

    fun onNameChange(name: String) {
        _state.update { it.copy(name = name) }
    }

    fun onTabSelect(tab: Int) {
        _state.update { it.copy(selectedTab = tab) }
    }

    fun onPhoneChange(input: String) {
        // 숫자만 추출
        val numbersOnly = input.filter { it.isDigit() }

        // 11자리 제한
        if (numbersOnly.length <= 11) {
            // 하이픈 추가 포맷팅
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

            _state.update {
                it.copy(
                    phone = formatted,
                    isPhoneValid = numbersOnly.length == 11
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

    private fun verifyCode() {
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
