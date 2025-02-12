package com.ssafy.keywe.viewmodel

//import com.ssafy.keywe.presentation.profile.state.AddMemberState
//import com.ssafy.keywe.presentation.profile.state.VerificationStatus
import android.net.Uri
import android.util.Log
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.profile.PostProfileRequest
import com.ssafy.keywe.data.state.AddMemberState
import com.ssafy.keywe.data.state.VerificationStatus
import com.ssafy.keywe.domain.profile.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMemberViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AddMemberState())
    val state = _state.asStateFlow()

    private val _profileAddedEvent = MutableSharedFlow<PostProfileRequest>()
    val profileAddedEvent = _profileAddedEvent.asSharedFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _phoneTextFieldValue = MutableStateFlow(TextFieldValue(""))
    val phoneTextFieldValue = _phoneTextFieldValue.asStateFlow()

    private val _isAddButtonEnabled = MutableStateFlow(false)
    val isAddButtonEnabled = _isAddButtonEnabled.asStateFlow()

    // 인증번호 입력
    private val _verificationMessage = MutableStateFlow<String>("")
    val verificationMessage = _verificationMessage.asStateFlow()

    private val _isVerificationButtonEnabled = MutableStateFlow(false)
    val isVerificationButtonEnabled = _isVerificationButtonEnabled.asStateFlow()

    // 이미지 처리
    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri = _profileImageUri.asStateFlow()

    fun postProfile() {
        viewModelScope.launch {
            try {
                Log.d("state 상태", "$state.value")
                Log.d("_state 상태", "$_state.value")
                val phoneNumber = _state.value.phone.replace("-", "")
                val request = PostProfileRequest(
                    role = if (_state.value.selectedTab == 0) "PARENT" else "CHILD",
                    name = _state.value.name,
                    phone = if (_state.value.selectedTab == 0) phoneNumber else "",
//                    password = if (_state.value.selectedTab == 0) _state.value.password.toInt() else 0
                    password = if (_state.value.selectedTab == 0) _state.value.password else ""
                )
                Log.d("Add Member", "$request")

                when (val result = repository.postProfile(request)) {
                    is ResponseResult.Success -> {
                        _profileAddedEvent.emit(request)
//                        clearState()
                        AddMemberState()
                        Log.d("Add Member", "성공함")
                    }

                    is ResponseResult.ServerError -> {
                        _errorMessage.value = "프로필 생성에 실패했습니다"
                        Log.d("Add Member", "서버 문제 실패")

                    }

                    is ResponseResult.Exception -> {
                        _errorMessage.value = result.message
                        Log.d("Add Member", "뭔지 모를 오류")

                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "네트워크 오류가 발생했습니다"
            }
        }
    }

    private fun checkAddButtonEnabled() {
        val currentState = _state.value
        _isAddButtonEnabled.value = if (currentState.selectedTab == 0) {
            // 부모인 경우
            currentState.name.isNotEmpty() &&
                    currentState.isPhoneValid &&
                    currentState.verificationStatus == VerificationStatus.SUCCESS &&
                    currentState.password.length == 4
        } else {
            // 자녀인 경우
            currentState.name.isNotEmpty()
        }
    }

    fun onNameChange(name: String) {
        _state.update {
            it.copy(name = name)
        }
        checkAddButtonEnabled()
    }

    fun onTabSelect(tab: Int) {
        _state.update {
            it.copy(
                selectedTab = tab,
                phone = "",
                verificationCode = "",
                password = if (tab == 1) "" else it.password
            )
        }
        checkAddButtonEnabled()
    }

    fun onPhoneChange(input: TextFieldValue) {
        if (_state.value.selectedTab == 0) {
            val newNumbersOnly = input.text.filter { it.isDigit() }
            if (newNumbersOnly.length <= 11) {
                val formatted = when {
                    newNumbersOnly.length <= 3 -> newNumbersOnly
                    newNumbersOnly.length <= 7 -> "${newNumbersOnly.take(3)}-${
                        newNumbersOnly.substring(
                            3
                        )
                    }"

                    else -> "${newNumbersOnly.take(3)}-${
                        newNumbersOnly.substring(
                            3,
                            7
                        )
                    }-${newNumbersOnly.substring(7)}"
                }

                _state.update {
                    it.copy(
                        phone = formatted,
                        isPhoneValid = newNumbersOnly.length == 11
                    )
                }
                _phoneTextFieldValue.value = TextFieldValue(
                    text = formatted,
                    selection = TextRange(formatted.length)
                )
                checkAddButtonEnabled()
            }
        }
    }

    fun onSimplePasswordChange(password: String) {
        if (_state.value.selectedTab == 0 && password.length <= 4 && password.all { it.isDigit() }) {
            _state.update {
                it.copy(password = password)
            }
            checkAddButtonEnabled()
        }
    }

    private fun clearState() {
        _state.update {
            it.copy(
                name = "",
                phone = "",
                verificationCode = "",
                password = "",
                selectedTab = 0,
                isPhoneValid = false,
                isVerificationSent = false,
                verificationStatus = VerificationStatus.NONE
            )
        }
        _phoneTextFieldValue.value = TextFieldValue("")
//        _profileImageUri.value = null
//        _verificationMessage.value = ""
//        _isVerificationButtonEnabled.value = false
        _isAddButtonEnabled.value = false
    }

    fun verifyCode() {
        viewModelScope.launch {
            if (state.value.verificationCode.length == 6) {
                // 인증번호 검증 API 호출
                val isSuccess = state.value.verificationCode == "123456"  // 임시 검증 로직
                _state.update {
                    it.copy(
                        verificationStatus = if (isSuccess) VerificationStatus.SUCCESS
                        else VerificationStatus.FAILURE
                    )
                }
                _verificationMessage.value = if (isSuccess) {
                    "인증이 성공되었습니다."
                } else {
                    "인증이 실패되었습니다."
                }
                checkAddButtonEnabled()
            }
        }
    }

    fun onVerificationCodeChange(code: String) {
        if (code.length <= 6 && code.all { it.isDigit() }) {
            _state.update { it.copy(verificationCode = code) }
            _isVerificationButtonEnabled.value = code.length == 6
        }
    }

    fun sendVerification() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isVerificationSent = true, verificationStatus = VerificationStatus.NONE
                )
            }
            // 인증번호 전송 API 호출
            _verificationMessage.value = "인증번호가 전송되었습니다."
        }
    }

    fun updateProfileImage(uri: Uri) {
        _profileImageUri.value = uri
    }


}

