package com.ssafy.keywe.presentation.kiosk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.domain.order.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KioskViewModel @Inject constructor() : ViewModel() {

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> get() = _phoneNumber

    private val _isCheckProfileDialogOpen = MutableStateFlow(false)
    val isCheckProfileDialogOpen: StateFlow<Boolean> = _isCheckProfileDialogOpen

    private val _inputPassword = MutableStateFlow("")
    val inputPassword: StateFlow<String> get() = _inputPassword

    fun updatePhoneNumber(number: String) {
        viewModelScope.launch {
            _phoneNumber.value = number
        }
    }

    fun closeCheckProfileDialog() {
        _isCheckProfileDialogOpen.value = false
    }

    fun openCheckProfileDialog() {
        _isCheckProfileDialogOpen.value = true
    }

    fun updateInputPassword(password: String) {
        _inputPassword.value = password
    }

    fun clearInputPassword() {
        _inputPassword.value = ""
    }
}
