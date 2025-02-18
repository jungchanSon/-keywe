package com.ssafy.keywe.presentation.profile.viewmodel

//import com.ssafy.keywe.data.dto.profile.GetProfileRequest
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.manager.ProfileIdManager
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.data.datastore.ProfileDataStore
import com.ssafy.keywe.data.dto.profile.UpdateProfileRequest
import com.ssafy.keywe.domain.profile.ProfileRepository
import com.ssafy.keywe.presentation.profile.state.EditMemberState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditMemberViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val profileDataStore: ProfileDataStore,
    private val tokenManager: TokenManager,
) : ViewModel() {

    private val _state = MutableStateFlow(EditMemberState())
    val state = _state.asStateFlow()

    // 휴대폰 번호 입력을 위한 TextField 상태
    private val _phoneTextFieldValue = MutableStateFlow(TextFieldValue(""))
    val phoneTextFieldValue = _phoneTextFieldValue.asStateFlow()

    // 이미지 처리를 위한 상태 추가
    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri = _profileImageUri.asStateFlow()

    init {
        viewModelScope.launch {
        }
    }

    //    fun updateProfileImage(uri: Uri) {
//        _profileImageUri.value = uri
//        _state.update {
//            it.copy(isModified = true)
//        }
//    }
    fun updateProfileImage(uri: Uri) {
        _profileImageUri.value = uri
        _state.update {
            it.copy(
                profileImage = uri.toString(),
                isModified = true
            )
        }
    }

    fun onNameChange(name: String) {
        _state.update {
            it.copy(
                name = name, isModified = true
            )
        }
    }

    fun onPhoneChange(phone: String) {
        val numbersOnly = phone.filter { it.isDigit() }

        if (numbersOnly.length <= 11) {
            val formatted = when {
                numbersOnly.length <= 3 -> numbersOnly
                numbersOnly.length <= 7 -> "${numbersOnly.take(3)}-${numbersOnly.substring(3)}"
                else -> "${numbersOnly.take(3)}-${
                    numbersOnly.substring(
                        3, 7
                    )
                }-${numbersOnly.substring(7)}"
            }
            // 커서 위치 계산
            val newCursorPosition = formatted.length.coerceAtMost(formatted.length)

            _state.update {
                it.copy(
                    phone = formatted, isModified = true
                )
            }
            // TextFieldValue 업데이트
            _phoneTextFieldValue.value = TextFieldValue(
                text = formatted, selection = TextRange(newCursorPosition)
            )
        }
    }

    fun onSimplePasswordChange(password: String) {
        if (password.length <= 4 && password.all { it.isDigit() }) {
            _state.update {
                it.copy(
                    password = password, isModified = true
                )
            }
        }
    }

    fun loadProfile() {
        viewModelScope.launch {
            ProfileIdManager.profileId.value?.let { profileId ->
                when (val result = repository.getProfileDetail(profileId)) {
                    is ResponseResult.Success -> {
                        val profile = result.data
                        val formattedPhone = profile.phone ?: ""

                        _state.update {
                            it.copy(
                                id = profile.id,
                                name = profile.name,
                                role = profile.role ?: "PARENT",
                                phone = formattedPhone,
                                password = "", // profile.password ?: ""  비밀번호는 보안 상 불러오지 않음
                                profileImage = profile.image,
                                isModified = false
                            )
                        }

                        // ✅ 기존 핸드폰 번호를 `_phoneTextFieldValue`에도 반영
                        _phoneTextFieldValue.value = TextFieldValue(text = formattedPhone)
                    }

                    is ResponseResult.ServerError -> {
                        Log.e("EditProfile", "서버 오류")
                    }

                    is ResponseResult.Exception -> {
                        Log.e("EditProfile", "네트워크 오류: ${result.message}")
                    }
                }
            }
        }
    }


    //수정 업데이트 정보 담기
    fun updateProfile(
        context: Context,
        profileViewModel: ProfileViewModel,
        navController: NavController,
    ) {
        viewModelScope.launch {
            val request = if (state.value.role == "PARENT") {
                UpdateProfileRequest(
                    name = state.value.name,
                    phone = state.value.phone.replace("-", ""),
                    password = state.value.password
                )
            } else {
                UpdateProfileRequest(name = _state.value.name)
            }

            // 이미지 처리: Uri를 MultipartBody.Part로 변환
            val profileImage = _profileImageUri.value?.let {
                createMultipartImage(context, it)
            }

            when (val result = repository.updateProfile(request, context, profileImage)) {
                is ResponseResult.Success -> {
                    Log.d("EditProfile", "프로필 수정 성공")
                    _state.update { it.copy(isModified = false) }

                    // 프로필 목록과 상세 정보 갱신
                    profileViewModel.refreshProfileList()
                    loadProfile()

                    // 프로필 화면으로 이동
                    navController.navigate(Route.ProfileBaseRoute.ProfileScreenRoute) {
                        popUpTo(Route.ProfileBaseRoute.ProfileEditRoute) { inclusive = true }
                    }
                }

                is ResponseResult.ServerError -> {
                    Log.e("EditProfile", "서버 오류 발생")
                }

                is ResponseResult.Exception -> {
                    Log.e("EditProfile", "네트워크 오류 발생: ${result.message}")
                }
            }
        }
    }

    fun createMultipartImage(context: Context, imageuri: Uri): MultipartBody.Part {
        val file = File(context.cacheDir, "profile_image.jpg").apply {
            context.contentResolver.openInputStream(imageuri)?.use { input ->
                outputStream().use { output -> input.copyTo(output) }
            }
        }
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }


    fun deleteProfile(profileId: Long, navController: NavController?) {
        viewModelScope.launch {
            val token = "Bearer ${tokenManager.getAccessToken()}"
            when (val result = repository.deleteProfile(profileId, token)) {
                is ResponseResult.Success -> {
                    Log.d("deleteProfile", "프로필 삭제 성공")
                    ProfileIdManager.clearProfileId() // 프로필 ID 초기화
                    tokenManager.clearTokens()
//                    _uiEvent.emit(UiEvent.ShowToast("프로필이 삭제되었습니다."))

                    // 로그인 화면으로 이동하는 로직 추가
                    navController?.navigate(Route.AuthBaseRoute.LoginRoute) {
                        popUpTo(0) { inclusive = true } // 백스택 전체 제거
                    }
                }

                is ResponseResult.ServerError -> {
                    Log.e("deleteProfile", "서버 오류로 프로필 삭제 실패")
                }

                is ResponseResult.Exception -> {
                    Log.e("deleteProfile", "네트워크 오류로 프로필 삭제 실패: ${result.message}")
                }
            }
        }
    }
}


