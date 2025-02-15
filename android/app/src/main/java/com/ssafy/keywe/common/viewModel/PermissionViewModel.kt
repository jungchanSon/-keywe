package com.ssafy.keywe.common.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.datastore.ProfileDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val profileDataStore: ProfileDataStore,
) : ViewModel() {
    fun saveIsFirstJoin(isFirstJoin: Boolean) {
        viewModelScope.launch {
            profileDataStore.saveIsFirstJoin(isFirstJoin)
        }
    }
}