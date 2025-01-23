package com.ssafy.keywe.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ssafy.keywe.data.dto.ErrorResponse
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


object KeyWeClient {
    private val BASE_URL = "https://dev.makeittakeit.kr"

    private val provideLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

//    private val provideTokenManager = ITokenManger()

    // todo 헤더 토큰 인터셉터 추가
    private val provideHttpClient =
        OkHttpClient.Builder()
            //.addInterceptor(TokenInterceptor(provideTokenManager))
            .addInterceptor(provideLoggingInterceptor)
//            .addInterceptor(AuthInterceptor(provideTokenManager))
//            .authenticator(AuthAuthenticator(provideTokenManager))
            .build()

    private val jsonBuilder = Json { coerceInputValues = true }

    private val provideRetrofit =
        Retrofit.Builder().baseUrl(BASE_URL).client(provideHttpClient).addConverterFactory(
            jsonBuilder.asConverterFactory("application/json".toMediaType())
        ).build()

    fun getErrorResponse(errorBody: ResponseBody): ErrorResponse {
        return provideRetrofit.responseBodyConverter<ErrorResponse>(
            ErrorResponse::class.java,
            ErrorResponse::class.java.annotations,
        ).convert(errorBody) ?: throw IllegalArgumentException("errorBody를 변환할 수 없습니다.")
    }

    fun <T> create(service: Class<T>): T {
        return provideRetrofit.create(service)
    }
}

