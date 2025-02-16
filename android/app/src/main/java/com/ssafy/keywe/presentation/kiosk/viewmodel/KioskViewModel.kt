package com.ssafy.keywe.presentation.kiosk.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.manager.ProfileIdManager
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.domain.order.OrderRepository
import com.ssafy.keywe.domain.order.VerificationUserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KioskViewModel @Inject constructor(
    private val repository: OrderRepository,
    private val tokenManager: TokenManager,
) : ViewModel() {

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> get() = _phoneNumber

    private val _isCheckProfileDialogOpen = MutableStateFlow(false)
    val isCheckProfileDialogOpen: StateFlow<Boolean> = _isCheckProfileDialogOpen

    private val _inputPassword = MutableStateFlow("")
    val inputPassword: StateFlow<String> get() = _inputPassword

    private val _verificationError = MutableStateFlow<String?>(null)
    val verificationError: StateFlow<String?> = _verificationError

    fun updatePhoneNumber(number: String) {
        _phoneNumber.value = number  // 즉시 업데이트
        Log.d("KioskViewModel", "updatePhoneNumber 완료됨: ${_phoneNumber.value}")  // 올바른 로그 출력
    }

    fun closeCheckProfileDialog() {
        _isCheckProfileDialogOpen.value = false
    }

    fun openCheckProfileDialog() {
        _isCheckProfileDialogOpen.value = true
    }

    fun updateInputPassword(password: String) {
        _inputPassword.value = password
    }

    fun clearInputPassword() {
        _inputPassword.value = ""
    }

    fun verifyUser(navController: NavController) {
        viewModelScope.launch {
            val phone = phoneNumber.value  // 최신 값 바로 가져오기
            Log.d("KioskViewModel", "최종 phone 값: $phone")

            if (phone.isEmpty()) {
                Log.e("KioskViewModel", "폰 번호가 설정되지 않음!")
                _verificationError.value = "전화번호가 올바르지 않습니다."
                return@launch
            }

            val password = _inputPassword.value
            Log.d("KioskViewModel", "API 요청: phone=$phone, password=$password")

            try {
                val response = repository.verificationUser(VerificationUserModel(phone, password))

                if (response is ResponseResult.Success) {
                    Log.d("KioskViewModel", "API 응답 성공: ${response.data}")
                    val profileId = response.data.profileId
                    val token = response.data.accessToken

                    tokenManager.saveKeyWeToken(token)
                    ProfileIdManager.updateProfileId(profileId.toLong())

                    navController.navigate(Route.MenuBaseRoute.ParentWaitingRoomRoute)
                } else {
                    Log.e("KioskViewModel", "API 응답 실패")
                    _verificationError.value = "회원 정보가 일치하지 않습니다."
                }
            } catch (e: Exception) {
                Log.e("KioskViewModel", "API 호출 중 오류 발생", e)
                _verificationError.value = "네트워크 오류 발생"
            }
        }
    }


}
