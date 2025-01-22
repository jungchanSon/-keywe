package com.ssafy.keywe.data.dto.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("token") val token: String,
)

/*
{
  "userId": 1,
  "id": 1,
  "title": "delectus aut autem",
  "completed": false
}
 */
@Serializable
data class TestResponse(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean,
)