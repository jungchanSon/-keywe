package com.ssafy.keywe.data.dto.auth

/*
{
  "email" : "ssafy1@ssafy.com",
  "password" : "Ssafy1234!"
}
 */
data class SignUpRequest(
    val email: String,
    val password: String,
)