package com.ssafy.keywe.presentation.profile.state

data class EmailVerificationState(
    val email: String = "",
    val verificationCode: String = "",
    val isEmailValid: Boolean = false,
    val isVerificationSent: Boolean = false,
    val isVerificationValid: Boolean = false
)