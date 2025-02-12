package com.ssafy.keywe.presentation.profile.state

import android.net.Uri

data class EditMemberState(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val password: String = "",
    val profileImage: Uri? = null,
    val isPhoneValid: Boolean = false,
    val isModified: Boolean = false,
    val role: String = ""
)