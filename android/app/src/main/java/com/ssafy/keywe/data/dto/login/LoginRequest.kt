package com.ssafy.keywe.data.dto.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
{
	"id" : string,
	"password" : string
}
 */
@Serializable
data class LoginRequest(
    @SerialName("id") val id: String,
    @SerialName("password") val password: String,
)
