package com.ssafy.keywe.viewmodel

//import com.ssafy.keywe.presentation.profile.state.AddMemberState
//import com.ssafy.keywe.presentation.profile.state.VerificationStatus
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
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


//    fun postProfile(
//        context: Context,
//        profileRequest: PostProfileRequest,
//        imageUri: Uri?,
//        onSuccess: (String) -> Unit,
//        onError: (String) -> Unit
//    ) {
//        viewModelScope.launch {
//            try {
//                val profileBody = createProfileRequestBody(profileRequest)
//                val imagePart = imageUri?.let { createMultipartImage(context, it) }
//
//                when (val result = repository.postProfile(profileBody, imagePart)) {
//                    is ResponseResult.Success -> {
//                        onSuccess(result.data.id) // 서버에서 반환된 id 사용
//                        Log.d("AddMemberViewModel", "✅ 프로필 추가 성공!")
//                    }
//
//                    is ResponseResult.ServerError -> {
//                        onError("❌ 서버 오류로 실패")
//                    }
//
//                    is ResponseResult.Exception -> {
//                        onError("❌ 네트워크 오류 발생")
//                    }
//                }
//            } catch (e: Exception) {
//                onError("❌ 예외 발생: ${e.message}")
//            }
//        }
//    }

    fun postProfile(
        context: Context,
        profileRequest: PostProfileRequest,
        imageUri: Uri?,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val gson = Gson()

            try {
                val role = if (_state.value.selectedTab == 0) "PARENT" else "CHILD"

                val profileJson = gson.toJson(
                    PostProfileRequest(
                        role = role,
                        name = _state.value.name,
                        phone = if (_state.value.selectedTab == 0) _state.value.phone?.replace(
                            "-",
                            ""
                        ) else null,
                        password = if (_state.value.selectedTab == 0) _state.value.password else null
                    )
                )
                val profileBody = profileJson.toRequestBody("application/json".toMediaTypeOrNull())

                val imageUri = _profileImageUri.value
                val profileImage = imageUri?.let { createMultipartImage(context, it) }

                when (val result =
                    repository.postProfile(profileBody, profileImage)) {
                    is ResponseResult.Success -> {
                        onSuccess(result.data.id)
                        clearState()
                        Log.d("AddMemberViewModel", "프로필 추가 성공")
                    }

                    is ResponseResult.ServerError -> {
                        _errorMessage.value = "프로필 생성에 실패했습니다"
                        Log.d("AddMemberViewModel", "서버 오류로 실패")
                    }

                    is ResponseResult.Exception -> {
                        _errorMessage.value = "네트워크 오류가 발생했습니다"
                        Log.d("AddMemberViewModel", "네트워크 오류")
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "네트워크 오류가 발생했습니다"
                Log.d("AddMemberViewModel", "예외 발생: ${e.message}")
            }
        }
    }

//    fun postProfile(
//        context: Context,
//        role: String,
//        name: String,
//        phone: String? = null,
//        password: String? = null,
//        imageUri: Uri?,
//        onSuccess: () -> Unit
//    ) {
//        viewModelScope.launch {
//            try {
//                val profileBody = createProfileRequestBody(role, name, phone, password)
//                val profileImage =
//                    imageUri?.let { createMultipartImage(context, it) }  // 이미지가 있으면 변환
//
//                when (val result = repository.postProfile(profileBody, profileImage)) {
//                    is ResponseResult.Success -> {
//                        onSuccess()
//                        Log.d("AddMemberViewModel", "프로필 추가 성공")
//                    }
//
//                    is ResponseResult.ServerError -> {
//                        Log.d("AddMemberViewModel", "서버 오류로 실패")
//                    }
//
//                    is ResponseResult.Exception -> {
//                        Log.d("AddMemberViewModel", "네트워크 오류")
//                    }
//                }
//            } catch (e: Exception) {
//                Log.d("AddMemberViewModel", "예외 발생: ${e.message}")
//            }
//        }
//    }

//    fun postProfile(context: Context, onSuccess: () -> Unit) {
//        viewModelScope.launch {
//            try {
//                Log.d("_state 상태", "$_state.value")
//                val phoneNumber = _state.value.phone.replace("-", "")
//                val imageBase64 = profileImageUri.value?.let { uri ->
//                    convertImageToBase64(context, uri)
//                    val request = PostProfileRequest(
//                        role = if (_state.value.selectedTab == 0) "PARENT" else "CHILD",
//                        name = _state.value.name,
//                        phone = if (_state.value.selectedTab == 0) phoneNumber else null,
////                    password = if (_state.value.selectedTab == 0) _state.value.password.toInt() else 0
//                        password = if (_state.value.selectedTab == 0) _state.value.password else null,
//                        imageBase64 = imageBase64
//                    )
//
//                    // 이미지 Base64 변환
//
////                val imageUri = _profileImageUri
////                val imagepart = imageUri?.let { uri ->
////                    val file = File(uri.path ?: "")
////                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
////                    MultipartBody.Part.createFormData("image", file.name, requestFile)
//                }
////                Log.d("Add Member", "$request")
//
//                //api 요청
//                when (val result = repository.postProfile(request)) {
//                    is ResponseResult.Success -> {
////                        _profileAddedEvent.emit(request)
//                        onSuccess()
////                        clearState()
//                        AddMemberState()
//                        Log.d("Add Member", "성공함")
//                    }
//
//                    is ResponseResult.ServerError -> {
//                        _errorMessage.value = "프로필 생성에 실패했습니다"
//                        Log.d("Add Member", "서버 문제 실패")
//
//                    }
//
//                    is ResponseResult.Exception -> {
//                        _errorMessage.value = result.message
//                        Log.d("Add Member", "뭔지 모를 오류")
//
//                    }
//                }
//            } catch (e: Exception) {
//                _errorMessage.value = "네트워크 오류가 발생했습니다"
//            }
//        }
//    }

    private fun createProfileRequestBody(profileRequest: PostProfileRequest): RequestBody {
        val gson = Gson()
        val json = gson.toJson(profileRequest)
        return json.toRequestBody("application/json".toMediaTypeOrNull())
    }


    // Uri -> 멀티파트로 바꾸는 함수
    private fun createMultipartImage(context: Context, uri: Uri): MultipartBody.Part {
        val file = File(context.cacheDir, "profile_image.jpg").apply {
            context.contentResolver.openInputStream(uri)?.use { input ->
                outputStream().use { output -> input.copyTo(output) }
            }
        }
        Log.d("AddMemberViewModel", " 이미지 파일 경로: ${file.absolutePath}, 크기: ${file.length()} bytes")

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

//    private fun createMultipartImage(context: Context, imageUri: Uri): MultipartBody.Part {
//        val inputStream = context.contentResolver.openInputStream(imageUri)
//        val file = File(context.cacheDir, "image.jpg").apply {
//            outputStream().use { output ->
//                inputStream?.copyTo(output)
//            }
//        }
//        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
//        return MultipartBody.Part.createFormData("image", file.name, requestFile)
//    }

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
            val phone = _state.value.phone.replace("-", "")
            val verificationCode = _state.value.verificationCode

            if (verificationCode.length != 6) {
                _errorMessage.value = "6자리 인증번호를 입력하세요."
                return@launch
            }

            when (val result = repository.verifySmsCode(phone, verificationCode)) {
                is ResponseResult.Success -> {
                    _state.update {
                        it.copy(verificationStatus = VerificationStatus.SUCCESS)
                    }
                    _verificationMessage.value = "인증이 성공되었습니다."
                    checkAddButtonEnabled()
                }

                is ResponseResult.ServerError -> {
                    _verificationMessage.value = "인증번호가 일치하지 않습니다."
                    _state.update { it.copy(verificationStatus = VerificationStatus.FAILURE) }
                }

                is ResponseResult.Exception -> {
                    _errorMessage.value = "네트워크 오류가 발생했습니다."
                }
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
            val phone = _state.value.phone.replace("-", "")

            // 전화번호 유효한지 확인
            if (phone.isEmpty() || phone.length != 11) {
                _errorMessage.value = "11자리 전화번호를 입력하세요."
                return@launch
            }

            when (val result = repository.sendSmsVerification(phone)) {
                is ResponseResult.Success -> {
                    _state.update {
                        it.copy(
                            isVerificationSent = true,
                            verificationStatus = VerificationStatus.NONE
                        )
                    }
                    _verificationMessage.value = "인증번호가 전송되었습니다."
                }

                is ResponseResult.ServerError -> {
                    _errorMessage.value = "SMS 전송에 실패했습니다."
                }

                is ResponseResult.Exception -> {
                    _errorMessage.value = "네트워크 오류가 발생했습니다."
                }
            }
        }
    }

    fun updateProfileImage(uri: Uri?) {
        _profileImageUri.value = uri
    }
}

