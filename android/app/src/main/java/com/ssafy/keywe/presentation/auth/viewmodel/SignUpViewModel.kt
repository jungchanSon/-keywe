package com.ssafy.keywe.presentation.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.ApiResponseHandler.onException
import com.ssafy.keywe.data.ApiResponseHandler.onServerError
import com.ssafy.keywe.data.ApiResponseHandler.onSuccess
import com.ssafy.keywe.data.dto.auth.SignUpRequest
import com.ssafy.keywe.domain.auth.AuthRepository
import com.ssafy.keywe.util.RegUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {


    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _passwordCheck = MutableStateFlow("")
    val passwordCheck: StateFlow<String> = _passwordCheck.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _validForm = MutableStateFlow(false)
    val validForm: StateFlow<Boolean> = _validForm.asStateFlow()

    private val _isSignUpIn = MutableStateFlow(false)
    val isSignIn: StateFlow<Boolean> = _isSignUpIn.asStateFlow()

    fun onEmailChanged(value: String) {
        _email.value = value
        validateForm()
    }

    fun onPasswordChanged(value: String) {
        _password.value = value
        validateForm()
        validPassword()
    }

    fun onPasswordCheckChanged(value: String) {
        _passwordCheck.value = value
        validateForm()
        validPassword()
    }

    private fun validateForm() {
        _validForm.value =
            Regex(RegUtil.EMAIL_REG).matches(_email.value) && _password.value.isNotEmpty() && _password.value == _passwordCheck.value
    }

    private fun validPassword() {
        if (_password.value != _passwordCheck.value) {
            _errorMessage.value = "비밀번호가 일치하지 않습니다."
        } else {
            _errorMessage.value = null
        }
    }

    fun signUp() {
        viewModelScope.launch {
            val request = SignUpRequest(
                email = _email.value, password = _password.value
            )
            repository.signUp(request).onSuccess { _isSignUpIn.update { true } }
                .onException { e, message -> {} }.onServerError { }
        }
    }
}