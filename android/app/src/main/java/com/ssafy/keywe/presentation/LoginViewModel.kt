package com.ssafy.keywe.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.ApiResponseHandler.onException
import com.ssafy.keywe.data.ApiResponseHandler.onServerError
import com.ssafy.keywe.data.ApiResponseHandler.onSuccess
import com.ssafy.keywe.data.auth.AuthRepository
import com.ssafy.keywe.data.dto.Status
import com.ssafy.keywe.data.dto.auth.MITILoginRequest
import com.ssafy.keywe.domain.LoginModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {
    private val _errorMessage = mutableStateOf("")


    suspend fun loginMITI() {
        val loginRequest = MITILoginRequest(
            email = "testuser2@makeittakeit.kr", password = "Miti1234!"
        )

        viewModelScope.launch {
            repository.login(loginRequest)
                .onSuccess(::saveUserToken)
                .onServerError(::handleError)
                .onException(::handleException)
        }
    }

    private fun saveUserToken(
        newToken: LoginModel,
    ) {
        Log.d("token", newToken.toString())
        viewModelScope.launch {
//            StaccatoApplication.userInfoPrefsManager.setToken(newToken)
//            _isLoginSuccess.postValue(true)
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
}