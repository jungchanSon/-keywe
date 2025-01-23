package com.ssafy.keywe.data

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token: String = runBlocking {
            tokenManager.getAccessToken().first().toString()
        }
//        (runBlocking {
//            tokenManager.getAccessToken().first()
//        } ?: return errorResponse(chain.request())).toString()

        val request = chain.request().newBuilder().header(AUTHORIZATION, "Bearer $token").build()

        ////////////////////////////////// 여기부터 추가된 코드 /////////////////////////////////////
        val response = chain.proceed(request)
        if (response.isSuccessful) {
            val newAccessToken: String = response.header(AUTHORIZATION, null) ?: return response
            Log.d("AUTH", "new Access Token = $newAccessToken")

            CoroutineScope(Dispatchers.IO).launch {
                val existedAccessToken = tokenManager.getAccessToken().first()
                if (existedAccessToken != newAccessToken) {
                    tokenManager.saveAccessToken(newAccessToken)
                    Log.d(
                        "AUTH", "newAccessToken = ${newAccessToken}\n" +
                                "ExistedAccessToken = $existedAccessToken"
                    )
                }
            }
        } else {
            Log.d(
                "AUTH", "${response.code} : ${response.request} \n" +
                        " ${response.message}"
            )
        }

        return response
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
    }

}

class AuthAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking {
            tokenManager.getRefreshToken().first()
        }

        if (refreshToken == null || refreshToken == "LOGIN") {
            response.close()
            return null
        }

        return newRequestWithToken(refreshToken, response.request)
    }

    private fun newRequestWithToken(token: String, request: Request): Request =
        request.newBuilder()
            .header(AUTHORIZATION, token)
            .build()

    companion object {
        private const val AUTHORIZATION = "Authorization"
    }
}