package com.ssafy.keywe.presentation.order.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OrderAppBarViewModel @Inject constructor() : ViewModel() {
    private val _speakerSound = MutableStateFlow(true)
    val speakerSound: StateFlow<Boolean> = _speakerSound

    private val _isStopCallingDialogOpen = MutableStateFlow(false)
    val isStopCallingDialogOpen: StateFlow<Boolean> = _isStopCallingDialogOpen.asStateFlow()

    fun toggleSpeaker() {
        _speakerSound.update { !it }
    }

    fun toggleUnconnect() {
        _isStopCallingDialogOpen.update { !it }
    }
}
