package com.ssafy.keywe.data.login

import com.ssafy.keywe.data.dto.login.LoginRequest
import com.ssafy.keywe.data.dto.login.LoginResponse
import com.ssafy.keywe.data.dto.login.MITILoginRequest
import com.ssafy.keywe.data.dto.login.MITILoginResponse
import com.ssafy.keywe.data.dto.login.MITIResponse
import com.ssafy.keywe.data.dto.login.TestResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

//https://jsonplaceholder.typicode.com/todos/1
interface LoginApiService {
    @POST(LOGIN_PATH)
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): Response<LoginResponse>

    @GET(TEST)
    suspend fun test(): Response<TestResponse>

    @POST(LOGIN_MITI)
    suspend fun loginMiti(@Body loginRequest: MITILoginRequest)
            : Response<MITIResponse<MITILoginResponse>>


    companion object {
        private const val LOGIN_PATH = "/login"
        private const val TEST = "/todos/1"
        private const val LOGIN_MITI = "/auth/login/email"
    }
}