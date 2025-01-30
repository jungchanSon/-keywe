package com.ssafy.keywe.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.presentation.profile.state.EmailVerificationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailVerificationViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(EmailVerificationState())
    val state = _state.asStateFlow()

    fun onEmailChange(email: String) {
        _state.update {
            it.copy(
                email = email,
                isEmailValid = isValidEmail(email)
            )
        }
    }

    fun onVerificationCodeChange(code: String) {
        _state.update { it.copy(verificationCode = code) }
    }

    fun sendVerification() {
        viewModelScope.launch {
            _state.update { it.copy(isVerificationSent = true) }
        }
    }
}
