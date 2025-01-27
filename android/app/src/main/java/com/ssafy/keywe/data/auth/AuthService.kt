package com.ssafy.keywe.data.auth

import com.ssafy.keywe.data.dto.auth.LoginRequest
import com.ssafy.keywe.data.dto.auth.LoginResponse
import com.ssafy.keywe.data.dto.auth.MITILoginRequest
import com.ssafy.keywe.data.dto.auth.MITILoginResponse
import com.ssafy.keywe.data.dto.auth.MITIResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


//https://jsonplaceholder.typicode.com/todos/1
interface AuthService {
    @POST(LOGIN_PATH)
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): Response<LoginResponse>

    @POST(LOGIN_MITI)
    suspend fun loginMiti(@Body loginRequest: MITILoginRequest)
            : Response<MITIResponse<MITILoginResponse>>


    companion object {
        private const val LOGIN_PATH = "/login"
        private const val LOGIN_MITI = "/auth/login/email"
    }
}