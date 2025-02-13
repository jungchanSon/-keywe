package com.ssafy.keywe.common.manager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object ProfileIdManager {
    private val _profileId = MutableStateFlow<Long?>(null)
    val profileId = _profileId.asStateFlow()

    // 프로필 id 업데이트
    fun updateProfileId(id: Long) {
        _profileId.update { id }
    }

    // 프로필 id 초기화
    fun clearProfileId() {
        _profileId.update { null }
    }
}

