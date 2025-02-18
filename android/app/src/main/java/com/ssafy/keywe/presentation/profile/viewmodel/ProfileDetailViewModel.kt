package com.ssafy.keywe.presentation.profile.viewmodel

//import com.ssafy.keywe.data.dto.profile.GetProfileRequest
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.gson.Gson
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.manager.ProfileIdManager
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.profile.UpdateProfileRequest
import com.ssafy.keywe.domain.profile.GetProfileDetailModel
import com.ssafy.keywe.domain.profile.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

//@HiltViewModel
//class ProfileDetailViewModel @Inject constructor(
//    private val repository: ProfileRepository
//) : ViewModel() {
//
//    private val _profileData = MutableStateFlow<GetProfileDetailResponse?>(null)
//    val profiledetailData = _profileData.asStateFlow()
//
//    private val _state = MutableStateFlow<GetProfileDetailModel?>(null)
//    val state = _state.asStateFlow()
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading = _isLoading.asStateFlow()
//
//    private val _error = MutableStateFlow<String?>(null)
//    val error = _error.asStateFlow()
//
//    //GetProfileDetailModel 외 정보들을 EditMemberState에 가져오기
//    private val _password = MutableStateFlow<String?>("")
//    val password = _password.asStateFlow()
//    private val _isModified = MutableStateFlow(false)
//    val isModified = _isModified.asStateFlow() // 현재 프로필 수정이 이루어졌는지 추적하는 것
//
//
//    init {
//        loadProfileDetail()
//    }
//
//
//    fun onPasswordChange(newPassword: String) {
//        _password.value = newPassword
//    }
//
//    fun loadProfileDetail() {
//        viewModelScope.launch {
//            _isLoading.value = true
//            _error.value = null
//
//            val profileId = ProfileIdManager.profileId.value
//            if (profileId == null) {
//                Log.e("ProfileDetailViewModel", "❌ Profile ID is null, cannot fetch data!")
//                _isLoading.value = false
//                return@launch
//            }
//
//            Log.d("ProfileDetailViewModel", "✅ Fetching profile detail for ID: $profileId")
//
//            when (val result = repository.getProfileDetail(profileId)) {
//                is ResponseResult.Success -> {
//                    val profileData = result.data
//                    _state.value = profileData.copy(
//                        image = if (!profileData.image.isNullOrBlank()) profileData.image else null
//                    )
////                    _state.value = result.data // ✅ null 방지
////                    Log.d(
////                        "ProfileDetailViewModel",
////                        "✅ Profile data loaded successfully: ${_state.value}"
////                    )
//                }
//
//                is ResponseResult.ServerError -> {
//                    _error.value = "서버 오류가 발생했습니다"
//                    Log.e("ProfileDetailViewModel", " Server error while fetching profile data")
//                }
//
//                is ResponseResult.Exception -> {
//                    _error.value = "네트워크 오류가 발생했습니다"
//                    Log.e("ProfileDetailViewModel", " Network error: ${result.message}")
//                }
//            }
//            _isLoading.value = false
//        }
//    }
//
//
//    fun updateProfile(
//        context: Context,
//        profileViewModel: ProfileViewModel,
//        imageUri: Uri?,
//        navController: NavController
//    ) {
//        viewModelScope.launch {
//            val gson = Gson()
//            val currentState = state.value ?: return@launch  // state가 null이면 즉시 종료, 없어도 되긴할거각ㅌㅇ
//            val passwordValue = password.value
//
//            val request = if (currentState.role == "PARENT") {
//                UpdateProfileRequest(
//                    name = currentState.name,
//                    phone = currentState.phone ?: "".replace("-", ""),
//                    password = passwordValue
//                )
//            } else {
//                UpdateProfileRequest(
//                    name = currentState.name
//                )
//            }
//            val profileBody =
//                gson.toJson(request).toRequestBody("application/json".toMediaTypeOrNull())
//
//
//            when (val result = repository.updateProfile(request, context, imageUri)) {
//                is ResponseResult.Success -> {
//                    Log.d("EditProfile", "프로필 수정 성공")
//                    _state.update { it.copy(isModified.value = false) }
//                    _isModified.value = false // 상태 업데이트
//
//                    profileViewModel.refreshProfileList()
//                    loadProfileDetail()
//
//                    navController.popBackStack(
//                        Route.ProfileBaseRoute.ProfileScreenRoute,
//                        inclusive = true
//                    )
//                    navController.navigate(Route.ProfileBaseRoute.ProfileScreenRoute)
//
//                }
//
//                is ResponseResult.ServerError -> {
//                    Log.e("EditProfile", "서버 오류 발생")
//                }
//
//                is ResponseResult.Exception -> {
//                    Log.e("EditProfile", "네트워크 오류 발생: ${result.message}")
//                }
//            }
//        }
//    }
//
//    fun clearError() {
//        _error.value = null
//    }
//}
@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow<GetProfileDetailModel?>(null)
    val state = _state.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    init {
        loadProfileDetail()
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun loadProfileDetail() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val profileId = ProfileIdManager.profileId.value
            if (profileId == null) {
                Log.e("ProfileDetailViewModel", "❌ Profile ID is null, cannot fetch data!")
                _isLoading.value = false
                return@launch
            }

            Log.d("ProfileDetailViewModel", "✅ Fetching profile detail for ID: $profileId")

            when (val result = repository.getProfileDetail(profileId)) {
                is ResponseResult.Success -> {
                    val profileData = result.data
                    _state.value = profileData.copy(
                        image = profileData.image?.takeIf { it.isNotEmpty() }
                    )
                    Log.d(
                        "ProfileDetailViewModel",
                        "✅ Profile data loaded successfully: $profileData"
                    )
                }

                is ResponseResult.ServerError -> {
                    _error.value = "서버 오류가 발생했습니다"
                    Log.e("ProfileDetailViewModel", "❌ Server error while fetching profile data")
                }

                is ResponseResult.Exception -> {
                    _error.value = "네트워크 오류가 발생했습니다"
                    Log.e("ProfileDetailViewModel", "❌ Network error: ${result.message}")
                }
            }
            _isLoading.value = false
        }
    }

    fun updateProfile(
        context: Context,
        profileViewModel: ProfileViewModel,
        imageUri: Uri?,
        navController: NavController
    ) {
        viewModelScope.launch {
            val gson = Gson()
            val currentState = state.value ?: return@launch
            val passwordValue = password.value

            val request = if (currentState.role == "PARENT") {
                UpdateProfileRequest(
                    name = currentState.name,
                    phone = currentState.phone ?: "".replace("-", ""),
                    password = passwordValue
                )
            } else {
                UpdateProfileRequest(name = currentState.name)
            }

            // 이미지 MultipartBody.Part로 변환
            val imagePart: MultipartBody.Part? = imageUri?.let { createMultipartImage(context, it) }

            val profileBody =
                gson.toJson(request).toRequestBody("application/json".toMediaTypeOrNull())

            when (val result = repository.updateProfile(request, context, imagePart)) {
                is ResponseResult.Success -> {
                    Log.d("EditProfile", "✅ 프로필 수정 성공")

                    profileViewModel.refreshProfileList() // ✅ 최신 프로필 목록 가져오기
                    loadProfileDetail() // ✅ 최신 프로필 정보 가져오기

                    // ✅ 프로필 화면 새로고침 (백스택에서 제거 후 다시 이동)
                    navController.popBackStack(
                        Route.ProfileBaseRoute.ProfileScreenRoute,
                        inclusive = true
                    )
                    navController.navigate(Route.ProfileBaseRoute.ProfileScreenRoute) {
                        launchSingleTop = true
                    }
                }

                is ResponseResult.ServerError -> {
                    Log.e("EditProfile", "❌ 서버 오류 발생")
                }

                is ResponseResult.Exception -> {
                    Log.e("EditProfile", "❌ 네트워크 오류 발생: ${result.message}")
                }
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    private fun createMultipartImage(context: Context, uri: Uri): MultipartBody.Part {
        val file = File(context.cacheDir, "image.jpg").apply {
            context.contentResolver.openInputStream(uri)?.use { input ->
                outputStream().use { output -> input.copyTo(output) }
            }
        }
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

}