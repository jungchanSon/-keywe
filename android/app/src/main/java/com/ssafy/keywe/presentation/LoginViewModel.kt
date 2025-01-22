package com.ssafy.keywe.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.ApiResponseHandler
import com.ssafy.keywe.data.ApiResponseHandler.onException
import com.ssafy.keywe.data.ApiResponseHandler.onServerError
import com.ssafy.keywe.data.ApiResponseHandler.onSuccess
import com.ssafy.keywe.data.dto.Status
import com.ssafy.keywe.data.dto.login.MITILoginRequest
import com.ssafy.keywe.data.login.LoginApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginApiRepository,
) : ViewModel() {
//    suspend fun test() {
//        val response = loginApiRepository.test()
//        if (response.isSuccessful) {
//            response.body()
//            Log.d("test", response.body().toString())
//        }
//    }
    private val _errorMessage = mutableStateOf("")


    suspend fun loginMITI(){
        val loginRequest = MITILoginRequest(
            email = "testuser2@makeittakeit.kr",
            password = "Miti1234!"
        )
        viewModelScope.launch {
            repository.login(loginRequest)
//                .onSuccess(::saveUserToken)
//                .onServerError(::handleError)
                .onException(::handleException)
        }



//        ApiResponseHandler.handleApiResponse {
//            loginApiRepository.mitiLogin(loginRequest)
//        }
//        if(response.isSuccessful){
//            response.body()
//            Log.d("test", response.body().toString())
//        }else{
//            response.errorBody()
//            Log.d("error", response.errorBody().toString())
//        }
    }
    private fun saveUserToken(newToken: String) {
        viewModelScope.launch {
//            StaccatoApplication.userInfoPrefsManager.setToken(newToken)
//            _isLoginSuccess.postValue(true)
        }
    }

    private fun handleError(
        status: Status,
        errorMessage: String,
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