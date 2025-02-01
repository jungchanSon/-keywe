package com.ssafy.keywe.data.state

data class AddMemberState(
    val name: String = "",
    val phone: String = "",
    val verificationCode: String = "",
    val selectedTab: Int = 0,
    val isPhoneValid: Boolean = false,
    val isVerificationSent: Boolean = false,
    val isVerificationValid: Boolean = false,
    val verificationStatus: VerificationStatus = VerificationStatus.NONE,
<<<<<<< HEAD:android/app/src/main/java/com/ssafy/keywe/data/state/AddMemberState.kt
    val simplePassword: String = ""
=======
    val simplePassword: String = "",
    val phoneSelection: Int = 0,
>>>>>>> 15bb131 (profile screen):android/app/src/main/java/com/ssafy/keywe/presentation/profile/state/AddMemberState.kt
)

enum class VerificationStatus {
    NONE,
    SUCCESS,
    FAILURE
}

