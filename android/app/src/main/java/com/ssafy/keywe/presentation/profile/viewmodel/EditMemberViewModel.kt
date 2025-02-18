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
import com.google.gson.Gson
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
import okhttp3.RequestBody.Companion.toRequestBody
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

//    private val _profileId = MutableStateFlow(-1L) // ✅ 선언 및 기본값 설정
//    val profileId: StateFlow<Long> = _profileId.asStateFlow() // ✅ 외부에서 읽을 수 있도록 제공

    init {
        viewModelScope.launch {
            // ✅ 앱 실행 시 저장된 profileId 불러오기
//            profileDataStore.profileIdFlow.collect { id ->
//                _profileId.value = id
//            }
        }
    }

    fun updateProfileImage(uri: Uri) {
        _profileImageUri.value = uri
        _state.update {
            it.copy(isModified = true)
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


//    // 기존 데이터 로드
//    fun loadProfile() {
//        viewModelScope.launch {
////            val profileId = _profileId.value
////            if (profileId == null || profileId == -1L) {
////                Log.e("EditProfile", "프로필 ID가 없습니다.")
////                return@launch
////            } 프로필 아디이 사용할 경우
//            ProfileIdManager.profileId.value?.let { profileId ->
//                when (val result = repository.getProfileDetail(profileId)) {
//                    is ResponseResult.Success -> {
//                        val profile = result.data
//                        _state.update {
//                            it.copy(
//                                id = profile.id,
//                                name = profile.name,
//                                role = profile.role ?: "PARENT",
//                                phone = profile.phone ?: "",
//                                password = "", //profile.password ?:
//                                isModified = false
//                            )
//
//                        }
//                    }
//
//                    is ResponseResult.ServerError -> {
//                        Log.e("EditProfile", "서버 오류")
//                    }
//
//                    is ResponseResult.Exception -> {
//                        Log.e("EditProfile", "네트워크 오류: ${result.message}")
//                    }
//                }
//            }
//        }
//    }

    //수정 업데이트 정보 담기
    fun updateProfile(
        context: Context, profileViewModel: ProfileViewModel, navController: NavController,
    ) {
        viewModelScope.launch {

            val gson = Gson()

            val request = if (state.value.role == "PARENT") {
                val formattedPhone = state.value.phone.replace("-", "")
                UpdateProfileRequest(
                    name = state.value.name, phone = formattedPhone, password = state.value.password
                )
            } else {
                UpdateProfileRequest(
                    name = _state.value.name
                )
            }

            //json 문자열로 변환
            val profileJsonString = gson.toJson(request)
//            val profileBody = MultipartBody.Part.createFormData(
//                "profile",
//                null,
//                profileJsonString.toRequestBody("application/json".toMediaTypeOrNull())
//            )
            val profileBody =
                gson.toJson(request).toRequestBody("application/json".toMediaTypeOrNull())


            val imageBody = _profileImageUri.value?.let { uri ->
                createMultipartImage(context, uri)
            }

            when (val result = repository.updateProfile(request, context, _profileImageUri.value)) {
                is ResponseResult.Success -> {
                    Log.d("EditProfile", "프로필 수정 성공")
                    _state.update { it.copy(isModified = false) }

                    profileViewModel.refreshProfileList() //최신 프로필 목록 가져오기
                    loadProfile()// 최신 프로필 정보 가져오기

                    navController.navigate(Route.ProfileBaseRoute.ProfileScreenRoute)
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

    private fun createMultipartImage(context: Context, uri: Uri): MultipartBody.Part {
        val file = File(context.cacheDir, "profile_image.jpg").apply {
            context.contentResolver.openInputStream(uri)?.use { input ->
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


//    // API 연동
//    fun deleteProfile() {
//        viewModelScope.launch {
//            val profileId = ProfileIdManager.updateProfileId()
////            if (profileId == -1L) {
////                Log.e("EditProfile", "프로필 ID가 없습니다.")
////                return@launch
////            }
//
//            when (val result = repository.deleteProfile(profileId)) { // ✅ 삭제 시에만 profileId 사용
//                is ResponseResult.Success -> Log.d("EditProfile", "프로필 삭제 성공")
//                is ResponseResult.ServerError -> Log.e("EditProfile", "서버 오류 발생")
//                is ResponseResult.Exception -> Log.e("EditProfile", "네트워크 오류 발생: ${result.message}")
//            }
//        }
}


