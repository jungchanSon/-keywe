package com.ssafy.keywe.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ssafy.keywe.data.login.LoginApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginApiRepository: LoginApiRepository,
) : ViewModel() {
    suspend fun test() {
        val response = loginApiRepository.test()
        if (response.isSuccessful) {
            response.body()
            Log.d("test", response.body().toString())
        }
    }
}