package com.ssafy.keywe.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.presentation.profile.state.EditMemberState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMemberViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(EditMemberState())
    val state = _state.asStateFlow()

    fun onNameChange(name: String) {
        _state.update {
            it.copy(
                name = name,
                isModified = true
            )
        }
    }

    fun onPhoneChange(input: String) {
        val numbersOnly = input.filter { it.isDigit() }

        if (numbersOnly.length <= 11) {
            val formatted = when {
                numbersOnly.length <= 3 -> numbersOnly
                numbersOnly.length <= 7 -> "${numbersOnly.take(3)}-${numbersOnly.substring(3)}"
                else -> "${numbersOnly.take(3)}-${
                    numbersOnly.substring(
                        3,
                        7
                    )
                }-${numbersOnly.substring(7)}"
            }

            _state.update {
                it.copy(
                    phone = formatted,
                    isModified = true
                )
            }
        }
    }

    fun onSimplePasswordChange(password: String) {
        if (password.length <= 4 && password.all { it.isDigit() }) {
            _state.update {
                it.copy(
                    simplePassword = password,
                    isModified = true
                )
            }
        }
    }

    fun deleteProfile() {
        viewModelScope.launch {
            // 프로필 삭제 로직 구현
        }
    }

    fun updateProfile() {
        viewModelScope.launch {
            // 프로필 업데이트 로직 구현
        }
    }

    fun loadMemberData(memberId: String) {
        viewModelScope.launch {
            // 기존 회원 정보 로드 로직 구현
        }
    }
}
