package com.ssafy.keywe.presentation.profile.state

data class AddMemberState(
    val name: String = "",
    val phone: String = "",
    val verificationCode: String = "",
    val selectedTab: Int = 0,
    val isPhoneValid: Boolean = false,
    val isVerificationSent: Boolean = false,
    val isVerificationValid: Boolean = false,
    val verificationStatus: VerificationStatus = VerificationStatus.NONE,
    val simplePassword: String = "",
)

enum class VerificationStatus {
    NONE,
    SUCCESS,
    FAILURE
}

