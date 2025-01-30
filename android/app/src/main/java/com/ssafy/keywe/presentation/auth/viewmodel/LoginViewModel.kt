package com.ssafy.keywe.presentation.auth.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.ApiResponseHandler.onException
import com.ssafy.keywe.data.ApiResponseHandler.onServerError
import com.ssafy.keywe.data.ApiResponseHandler.onSuccess
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.data.dto.Status
import com.ssafy.keywe.data.dto.auth.LoginRequest
import com.ssafy.keywe.domain.auth.AuthRepository
import com.ssafy.keywe.domain.auth.LoginModel
import com.ssafy.keywe.util.JWTUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager,
) : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()


    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun onEmailChanged(email: String) {
        _email.value = email
    }

    fun onPasswordChanged(password: String) {
        _password.value = password
    }


    fun login() {
        val loginRequest = LoginRequest(
            email = _email.value, password = password.value
        )
        viewModelScope.launch {
            repository.login(loginRequest).onSuccess(::saveUserToken).onServerError(::handleError)
                .onException(::handleException)
        }
    }

    private fun saveUserToken(
        newToken: LoginModel,
    ) {
        _isLoggedIn.value = true
        Log.d("token", newToken.toString())

        JWTUtil.isTempToken(newToken.accessToken.split(" ")[1])

        viewModelScope.launch {
            tokenManager.saveTempToken(newToken.accessToken)
        }
    }

    private fun handleError(
        status: Status,
    ) {
//        _errorMessage.postValue(errorMessage)
    }

    private fun handleException(
        e: Throwable,
        errorMessage: String,
    ) {
//        _errorMessage.postValue(errorMessage)
    }

    suspend fun logout() {
        tokenManager.clearTokens()
    }
}