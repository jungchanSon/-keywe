package com.ssafy.keywe.presentation.profile.viewmodel


import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.common.manager.ProfileIdManager
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.domain.profile.GetProfileDetailModel
import com.ssafy.keywe.domain.profile.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val tokenManager: TokenManager
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

//    suspend fun fetchProfileWithRetry(
//        profileId: Long,
//        retryCount: Int = 3,
//        delayMillis: Long = 1000
//    ): ResponseResult<GetProfileDetailModel> {
//        repeat(retryCount) { attempt ->
//            when (val result = repository.getProfileDetail(profileId)) {
//                is ResponseResult.Success -> return result
//                is ResponseResult.ServerError -> {
//                    Log.e("ProfileDetailViewModel", "❌ Server error (attempt ${attempt + 1})")
//                }
//
//                is ResponseResult.Exception -> {
//                    Log.e(
//                        "ProfileDetailViewModel",
//                        "❌ Network error (attempt ${attempt + 1}): ${result.message}"
//                    )
//                }
//            }
//            delay(delayMillis) // 1초 대기 후 재시도
//        }
//        return ResponseResult.ServerError(Status(code = 500, message = "서버 오류 발생"))
//    }


    fun loadProfileDetail() {

        if (_isLoading.value) {  // ✅ 이미 로딩 중이면 실행하지 않음
            Log.d("ProfileDetailViewModel", "⏳ Already loading, skipping request.")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val token = tokenManager.getToken()
            Log.d("ProfileDetailViewModel", "🔑 Current Token: $token")

            if (token.isNullOrEmpty()) {
                Log.e("ProfileDetailViewModel", "❌ Token is missing! Cannot fetch profile data.")
                _error.value = "인증 오류: 다시 로그인해주세요."
                _isLoading.value = false
                return@launch
            }

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
                    Log.d("ProfileDetailViewModel", "🔍 Raw Profile Data from Server: $profileData")
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


    fun refreshProfile() {
        if (_isLoading.value == true) {
            Log.d("ProfileDetailViewModel", "⏳ Already loading, skipping refresh")
            return
        }
        Log.d("ProfileDetailViewModel", "🔄 Refreshing Profile Data")
        loadProfileDetail()
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