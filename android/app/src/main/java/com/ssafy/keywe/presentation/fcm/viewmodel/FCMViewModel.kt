package com.ssafy.keywe.presentation.fcm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.ApiResponseHandler.onException
import com.ssafy.keywe.data.ApiResponseHandler.onServerError
import com.ssafy.keywe.data.ApiResponseHandler.onSuccess
import com.ssafy.keywe.data.dto.Status
import com.ssafy.keywe.data.dto.fcm.FCMRequest
import com.ssafy.keywe.domain.fcm.FCMModel
import com.ssafy.keywe.domain.fcm.FCMRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FCMViewModel @Inject constructor(
    private val repository: FCMRepository,
) : ViewModel() {
    private val _token = MutableStateFlow<String?>(null)
    val token = _token.asStateFlow()

    private fun registToken(token: String) {
        val request = FCMRequest(token)
        viewModelScope.launch {
            repository.registFCM(request).onSuccess(::handleSuccess).onServerError(::handleError)
                .onException(::handleException)
        }
    }

    fun updateToken(token: String) {
        _token.update { token }
        registToken(token)
    }

    private fun handleSuccess(model: FCMModel) {
    }

    private fun handleError(
        status: Status,
    ) {
//        _errorMessage.update {
//            "일치하는 사용자 정보가 없습니다."
//        }
    }

    private fun handleException(
        e: Throwable,
        errorMessage: String,
    ) {
//        _errorMessage.postValue(errorMessage)
    }
}

