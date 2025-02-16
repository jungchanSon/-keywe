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
    }

    fun openDialog() {
        _isStopCallingDialogOpen.value = true
    }


}
