package com.ssafy.keywe.presentation.profile.state

import android.net.Uri

data class EditMemberState(
    val name: String = "",
    val phone: String = "",
    val simplePassword: String = "",
    val profileImage: Uri? = null,
    val isModified: Boolean = false
)