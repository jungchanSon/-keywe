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
        val token: String? = runBlocking {
            tokenManager.getToken()
        }
        val request = chain.request().newBuilder().header(AUTHORIZATION, "Bearer $token").build()
        Log.d("API REQUEST", "token = $token")
        ////////////////////////////////// 여기부터 추가된 코드 /////////////////////////////////////
        val response = chain.proceed(request)
//        if (response.isSuccessful) {
//            val newAccessToken: String = response.header(AUTHORIZATION, null) ?: return response
//            Log.d("AUTH", "new Access Token = $newAccessToken")
//
//            CoroutineScope(Dispatchers.IO).launch {
//                val existedAccessToken: String = tokenManager.getAccessToken().toString()
//                if (existedAccessToken != newAccessToken) {
//                    tokenManager.saveAccessToken(newAccessToken)
//                    Log.d(
//                        "AUTH",
//                        "newAccessToken = ${newAccessToken}\n" + "ExistedAccessToken = $existedAccessToken"
//                    )
//                }
//            }
//        } else {
//            Log.d(
//                "AUTH", "${response.code} : ${response.request} \n" + " ${response.message}"
//            )
//        }
        return response
    }
}

class AuthAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        Log.d("AUTH", "authenticatprivate val authService: AuthService,e")

        // 리프레쉬 토큰 가져오기
        val refreshToken: String? = runBlocking {
            tokenManager.getRefreshToken()
        }

        // 없으면 추가 작업 X
        if (refreshToken == null || refreshToken == "LOGIN") {
            response.close()
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
            val response = authService.login(loginRequest = LoginRequest("", ""))
            // Access Token 토큰 재발급 성공
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    newAccessToken = it.token
                    tokenManager.saveAccessToken(it.token)
                    tokenManager.saveRefreshToken(it.token)
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

    private fun newRequestWithToken(token: String, request: Request): Request =
        request.newBuilder().header(AUTHORIZATION, token).build()
}