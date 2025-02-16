package com.ssafy.keywe.presentation.order.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OrderAppBarViewModel @Inject constructor() : ViewModel() {

    private val _isStopCallingDialogOpen = MutableStateFlow(false)
    val isStopCallingDialogOpen: StateFlow<Boolean> = _isStopCallingDialogOpen.asStateFlow()


    fun closeDialog() {
        _isStopCallingDialogOpen.value = false

    private val _isKiWiMatching = MutableStateFlow(false)
    val isKiWiMatching: StateFlow<Boolean> = _isKiWiMatching.asStateFlow()

    fun toggleSpeaker() {
        _speakerSound.update { !it }
    }

    fun openDialog() {
        _isStopCallingDialogOpen.value = true
    }

    fun setKiWiMatching() {
        _isKiWiMatching.update { true }
    }

    fun cutKiWiMatching() {
        _isKiWiMatching.update { false }
    }
}
