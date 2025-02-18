package com.ssafy.keywe.data.profile

//import com.ssafy.keywe.data.dto.profile.GetProfileRequest
import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.mapper.toDomain
import com.ssafy.keywe.data.dto.profile.PostProfileRequest
import com.ssafy.keywe.data.dto.profile.UpdateProfileRequest
import com.ssafy.keywe.domain.profile.GetProfileDetailModel
import com.ssafy.keywe.domain.profile.GetProfileListModel
import com.ssafy.keywe.domain.profile.PostProfileModel
import com.ssafy.keywe.domain.profile.ProfileRepository
import com.ssafy.keywe.domain.profile.UpdateProfileModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource,
) : ProfileRepository {

    override suspend fun getProfileList(): ResponseResult<List<GetProfileListModel>> {
        return when (val result = profileDataSource.requestGetProfileList()) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e, EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success(result.data.map { it.toDomain() })
        }
    }

    companion object {
        private const val EXCEPTION_NETWORK_ERROR_MESSAGE =
            "네트워크 연결이 불안정합니다. 연결을 재설정한 후 다시 시도해 주세요."
    }

    override suspend fun getProfileDetail(profileId: Long): ResponseResult<GetProfileDetailModel> {
        return when (val result = profileDataSource.requestGetProfileDetail(profileId)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e, EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success(result.data!!.toDomain())
        }
    }

    /** ✅ postProfile 메서드 수정 */
    override suspend fun postProfile(
        profileBody: RequestBody,
        image: MultipartBody.Part?
    ): ResponseResult<PostProfileModel> {
//        val profileBody = createProfileRequestBody(postProfileRequest) // JSON 변환
//        val profileImage = imageUri?.let { createMultipartImage(context, it) }
        return when (val result = profileDataSource.requestPostProfile(profileBody, image)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e, EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success(result.data!!.toDomain())
        }
    }


    /** ✅ updateProfile 메서드 수정 */
    override suspend fun updateProfile(
        updateProfileRequest: UpdateProfileRequest, context: Context, imageUri: Uri?
    ): ResponseResult<UpdateProfileModel> {
        val gson = Gson()
        val profileJsonString = gson.toJson(updateProfileRequest)
//        val profileBody = createProfileRequestBody(updateProfileRequest)
//        val profileBody = MultipartBody.Part.createFormData(
//            "profile",
//            null,
//            profileJsonString.toRequestBody("application/json".toMediaTypeOrNull())
//        )
        val profileBody = profileJsonString.toRequestBody("application/json".toMediaTypeOrNull())

        val profileImage = imageUri?.let { createMultipartImage(context, it) }

        return when (val result =
            profileDataSource.requestUpdateProfile(profileBody, profileImage)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e, EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success(result.data!!.toDomain())
        }
    }

    override suspend fun deleteProfile(profileId: Long, token: String): ResponseResult<Unit> {
        return when (val result = profileDataSource.requestDeleteProfile(profileId, token)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e, EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> result
        }
    }

//    companion object {
//        private const val EXCEPTION_NETWORK_ERROR_MESSAGE =
//            "네트워크 연결이 불안정합니다. 연결을 재설정한 후 다시 시도해 주세요."
//    }

    override suspend fun sendSmsVerification(phone: String): ResponseResult<String> {
        return when (val result = profileDataSource.requestSendSmsVerification(phone)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e,
                EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success("SMS 인증번호가 전송되었습니다.")
        }
    }

    override suspend fun verifySmsCode(
        phone: String,
        verificationCode: String
    ): ResponseResult<String> {
        return when (val result = profileDataSource.requestVerifySmsCode(phone, verificationCode)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e,
                EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success("SMS 인증이 완료되었습니다.")
        }
    }


    /** ✅ JSON 변환: Gson을 활용하여 JSON을 RequestBody로 변환 */
    fun createProfileRequestBody(profile: PostProfileRequest): RequestBody {
        val gson = Gson()
        val json = gson.toJson(profile)
        return json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }

    /** ✅ Multipart 이미지 변환: Uri -> MultipartBody.Part */
    fun createMultipartImage(context: Context, imageUri: Uri): MultipartBody.Part {
        val file = File(context.cacheDir, "profile_image.jpg").apply {
            context.contentResolver.openInputStream(imageUri)?.use { input ->
                outputStream().use { output -> input.copyTo(output) }
            }
        }
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }
}