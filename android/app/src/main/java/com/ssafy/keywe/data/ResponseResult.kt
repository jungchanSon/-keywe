package com.ssafy.keywe.data

import com.ssafy.keywe.data.dto.Status

sealed interface ResponseResult<T : Any> {
    class Success<T : Any>(val data: T) : ResponseResult<T>

    class ServerError<T : Any>(val status: Status) : ResponseResult<T>

    class Exception<T : Any>(val e: Throwable, val message: String) : ResponseResult<T>
}