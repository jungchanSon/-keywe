package com.ssafy.keywe.data

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ssafy.keywe.data.auth.AuthService
import com.ssafy.keywe.data.dto.auth.LoginRequest
import com.ssafy.keywe.util.NetworkUtil
import com.ssafy.keywe.util.NetworkUtil.AUTHORIZATION
import com.ssafy.keywe.util.NetworkUtil.jsonBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        /**
         * 인증 토큰 가져오기
         */
        val request = chain.request()

//        if (isRequestWithToken(request.url.toUri().path)) {
        val token = runBlocking {
            tokenManager.getToken()
        }
        val newRequest = request.newBuilder().header("Authorization", "$token").build()
        return chain.proceed(newRequest)
//        }

//        return chain.proceed(request)
    }


    private fun isRequestWithToken(path: String): Boolean {
        return NetworkUtil.WITH_TOKEN.contains(path)
    }
}

class AuthAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        Log.d("authenticate", "call AuthAuthenticator")
//        if (response.request.header("Authorization") != null) {
//            val newToken = "tokenManager.getToken()" ?: return null
//            return response.request.newBuilder()
//                .header("Authorization", "Bearer $newToken")
//                .build()
//        }
//        return null


        Log.d("AUTH", "authenticatprivate val authService: AuthService,e")

        // 리프레쉬 토큰 가져오기
        val refreshToken: String? = runBlocking {
            tokenManager.getRefreshToken()
        }

        // 없으면 추가 작업 X
        if (refreshToken == null || isRequestWithToken(response.request.url.toUri().path)) {
//            response.close()
            return null
        }

        val provideLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // refresh token 으로 새 토큰 발급
        val okHttpClient = OkHttpClient().newBuilder().addInterceptor(provideLoggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS) // 연결 타임아웃 설정
            .readTimeout(30, TimeUnit.SECONDS) // 읽기 타임아웃 설정
            .writeTimeout(30, TimeUnit.SECONDS).build()

        val retrofit = Retrofit.Builder().baseUrl(NetworkUtil.BASE_URL).client(okHttpClient)
            .addConverterFactory(
                jsonBuilder.asConverterFactory("application/json".toMediaType())
            ).build()

        val authService = retrofit.create(AuthService::class.java)

        var newAccessToken: String? = null

        runBlocking {
            val response =
                authService.userLogin(loginRequest = LoginRequest("ssafy1@ssafy.com", "Ssafy1234!"))
            // Access Token 토큰 재발급 성공
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    newAccessToken = it.accessToken
                    tokenManager.saveAccessToken(it.accessToken)
                    tokenManager.saveRefreshToken(it.accessToken)
                }
            } else {
                // 재발급 실패 시 로그아웃
                tokenManager.clearTokens()
            }


        }
        Log.d("AUTH", "newAccessToken = $newAccessToken")

        // 새로 가져온 토큰이 없으면 재요청 X
        if (newAccessToken == null) return null

        return newRequestWithToken(newAccessToken!!, response.request)
    }

    private fun isRequestWithToken(path: String): Boolean {
        return NetworkUtil.WITH_TOKEN.contains(path)
    }

    private fun newRequestWithToken(token: String, request: Request): Request =
        request.newBuilder().header(AUTHORIZATION, token).build()
}