package com.ssafy.keywe.presentation.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.ApiResponseHandler.onException
import com.ssafy.keywe.data.ApiResponseHandler.onServerError
import com.ssafy.keywe.data.ApiResponseHandler.onSuccess
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.data.dto.Status
import com.ssafy.keywe.data.dto.auth.CEOLoginRequest
import com.ssafy.keywe.data.dto.auth.LoginRequest
import com.ssafy.keywe.domain.auth.AuthRepository
import com.ssafy.keywe.domain.auth.CEOLoginModel
import com.ssafy.keywe.domain.auth.LoginModel
import com.ssafy.keywe.util.RegUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _validForm = MutableStateFlow<Boolean>(false)
    val validForm: StateFlow<Boolean> = _validForm.asStateFlow()

    fun onEmailChanged(email: String) {
        _email.value = email
        validateForm()
    }

    fun onPasswordChanged(password: String) {
        _password.value = password
        validateForm()
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

    fun ceoLogin() {
        val loginRequest = CEOLoginRequest(
            email = _email.value, password = password.value
        )
        viewModelScope.launch {
            repository.ceoLogin(loginRequest).onSuccess(::saveCeoToken).onServerError(::handleError)
                .onException(::handleException)
        }
    }


    private fun validateForm() {
        _validForm.value =
            Regex(RegUtil.EMAIL_REG).matches(_email.value) && _password.value.isNotEmpty()
    }

    private fun saveUserToken(
        newToken: LoginModel,
    ) {
        _isLoggedIn.value = true
        Log.d("token", newToken.toString())
//        JWTUtil.isTempToken(newToken.accessToken)
        viewModelScope.launch {
            tokenManager.saveTempToken(newToken.accessToken)
            tokenManager.clearStoreId()
            tokenManager.isKiosk = false
        }
    }

    private fun saveCeoToken(
        model: CEOLoginModel,
    ) {
        _isLoggedIn.value = true
        Log.d("token", model.toString())
//        JWTUtil.isTempToken(newToken.accessToken)
        viewModelScope.launch {
            tokenManager.saveAccessToken(model.accessToken)
            tokenManager.saveStoreId(model.storeId)
            tokenManager.isKiosk = true
        }
    }

    private fun handleError(
        status: Status,
    ) {
        _errorMessage.update {
            "일치하는 사용자 정보가 없습니다."
        }
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