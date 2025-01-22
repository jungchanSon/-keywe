package com.ssafy.keywe.data

import com.ssafy.keywe.data.KeyWeClient.getErrorResponse
import com.ssafy.keywe.data.dto.ErrorResponse
import com.ssafy.keywe.data.dto.Status
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

object ApiResponseHandler {
    suspend fun <T : Any> handleApiResponse(execute: suspend () -> Response<T>): ResponseResult<T> {
        return try {
            val response: Response<T> = execute()
            val body: T? = response.body()

            when {
                response.isSuccessful -> ResponseResult.Success(body as T)
                else -> {
                    val errorBody: ResponseBody = response.errorBody()
                        ?: throw IllegalArgumentException("erroryBody를 찾을 수 없습니다.")

                    val errorResponse: ErrorResponse = getErrorResponse(errorBody)
                    ResponseResult.ServerError(
                        status = Status.Code(errorResponse.code),
                    )
                }
            }
        } catch (e: HttpException) {
            ResponseResult.ServerError(
                status = Status.Code(e.code())
            )
        } catch (e: Throwable) {
            ResponseResult.Exception(e, message = e.message.toString())
        }
    }

    suspend fun <T : Any> ResponseResult<T>.onSuccess(executable: suspend (T) -> Unit): ResponseResult<T> =
        apply {
            if (this is ResponseResult.Success<T>) {
                executable(data)
            }
        }

    suspend fun <T : Any> ResponseResult<T>.onServerError(
        executable: suspend (status: Status) -> Unit,
    ): ResponseResult<T> = apply {
        if (this is ResponseResult.ServerError<T>) {
            executable(status)
        }
    }

    suspend fun <T : Any> ResponseResult<T>.onException(executable: suspend (e: Throwable, message: String) -> Unit): ResponseResult<T> =
        apply {
            if (this is ResponseResult.Exception<T>) {
                executable(e, message)
            }
        }
}