//package com.ssafy.keywe.data
//
//import kotlinx.coroutines.runBlocking
//import okhttp3.Interceptor
//import okhttp3.Request
//import okhttp3.Response
//
//class TokenInterceptor(private val tokenManager: TokenManager) : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val token = runBlocking { tokenManager.getAccessToken() }
//
//
//        val request = if (!token.isNullOrEmpty()) {
//            chain.request().putTokenHeader(token)
//        } else {
//            chain.request()
//        }
//
//        val response =
//            chain.proceed(request)
//
//        response.isSuccessful
//
//        return chain.proceed(request)
//    }
//
//
//    private fun Request.putTokenHeader(token: String?): Request {
//        return this.newBuilder().addHeader(AUTHORIZATION, "Bearer $token").build()
//    }
//
//    companion object {
//        private const val AUTHORIZATION = "Authorization"
//    }
//}