package com.ssafy.keywe.presentation.auth.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {
    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _passwordCheck = mutableStateOf("")
    val passwordCheck: State<String> = _passwordCheck

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun onPasswordCheckChanged(value: String) {
        _passwordCheck.value = value
    }
}