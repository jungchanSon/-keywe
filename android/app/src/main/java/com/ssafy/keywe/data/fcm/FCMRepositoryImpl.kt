package com.ssafy.keywe.data.fcm

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.fcm.FCMRequest
import com.ssafy.keywe.data.dto.mapper.toDomain
import com.ssafy.keywe.domain.fcm.FCMModel
import com.ssafy.keywe.domain.fcm.FCMRepository
import javax.inject.Inject

class FCMRepositoryImpl @Inject constructor(
    private val fcmDataSource: FCMDataSource,
) : FCMRepository {

    companion object {
        private const val EXCEPTION_NETWORK_ERROR_MESSAGE =
            "네트워크 연결이 불안정합니다.\n연결을 재설정한 후 다시 시도해 주세요."
    }

    override suspend fun registFCM(request: FCMRequest): ResponseResult<FCMModel> {
        return when (val result = fcmDataSource.registFCM(request)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e, EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success(result.data!!.toDomain())
        }
    }

}