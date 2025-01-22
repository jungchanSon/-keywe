package com.ssafy.keywe.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


data class User(val id: String)
interface ApiService {
    @GET("user/{user}")
    fun getUser(@Path("user") userId: String): Call<User>
}