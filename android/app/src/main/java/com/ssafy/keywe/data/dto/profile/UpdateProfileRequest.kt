package com.ssafy.keywe.data.dto.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    @SerialName("name") val name: String,
    @SerialName("phone") val phone: String,
    @SerialName("simplePassword") val simplePassword: String
)


// json 직렬화를 위한 @Serializable을 하는 것
// json에서 사용될 필드 이름을 @SerialName으로 지정, 서버의 json 필드명과 코틀린 변수명이 다를 때 매핑 가능
// ?를 붙이는 이유는 null이 가능. 안 붙이면 null이 불가능 항상 값이 필요
