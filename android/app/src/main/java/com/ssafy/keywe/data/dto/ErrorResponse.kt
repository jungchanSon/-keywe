package com.ssafy.keywe.data.dto

import kotlinx.serialization.Serializable

/*

{
	"code": 201,
	"isSuccess": true,
	"result": {
		"menuId": number
	},
}
 */
@Serializable
data class ErrorResponse(
    val code: Int,
    val isSuccess: Boolean,
)
