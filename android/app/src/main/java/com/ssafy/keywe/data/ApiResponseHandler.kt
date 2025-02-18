package com.ssafy.keywe.data

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ssafy.keywe.data.dto.ErrorResponse
import com.ssafy.keywe.data.dto.Status
import com.ssafy.keywe.util.NetworkUtil
import com.ssafy.keywe.util.NetworkUtil.jsonBuilder
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object ApiResponseHandler {
    suspend fun <T : Any> handleApiResponse(execute: suspend () -> Response<T>): ResponseResult<T> {
        return try {
            val response: Response<T> = execute()

            when {
                response.isSuccessful -> {
                    when (response.code()) {
                        204 -> {
                            @Suppress("UNCHECKED_CAST")
                            ResponseResult.Success(Unit as T)
                        }

                        else -> {
                            val body = response.body()
                            ResponseResult.Success(
                                body ?: @Suppress("UNCHECKED_CAST") (Unit as T)
                            )
                        }
                    }
                }

                else -> {
                    val errorBody: ResponseBody = response.errorBody()
                        ?: throw IllegalArgumentException("errorBody를 찾을 수 없습니다.")
                    Log.d("rest api error", errorBody.toString())

                    ResponseResult.ServerError(
                        status = Status.Code(response.code())
                    )
                }
            }
        } catch (e: HttpException) {
            ResponseResult.ServerError(
                status = Status.Code(e.code())
            )
        }
//        catch (e: Throwable) {
//            ResponseResult.Exception(e, message = e.message.toString())
//        }
    }

    fun getErrorResponse(errorBody: ResponseBody): ErrorResponse {
        val okHttpClient =
            OkHttpClient().newBuilder().connectTimeout(30, TimeUnit.SECONDS) // 연결 타임아웃 설정
                .readTimeout(30, TimeUnit.SECONDS) // 읽기 타임아웃 설정
                .writeTimeout(30, TimeUnit.SECONDS).build()
        val retrofit = Retrofit.Builder().baseUrl(NetworkUtil.BASE_URL).client(okHttpClient)
            .addConverterFactory(
                jsonBuilder.asConverterFactory("application/json".toMediaType())
            ).build()
        return retrofit.responseBodyConverter<ErrorResponse>(
            ErrorResponse::class.java,
            ErrorResponse::class.java.annotations,
        ).convert(errorBody) ?: throw IllegalArgumentException("errorBody를 변환할 수 없습니다.")
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
                Log.d("rest api error onException", message)
                executable(e, message)
            }
        }
}