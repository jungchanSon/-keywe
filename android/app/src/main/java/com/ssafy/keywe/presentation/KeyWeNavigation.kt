package com.ssafy.keywe.presentation

import kotlinx.serialization.Serializable

@Serializable
object SplashScreen

@Serializable
object HomeScreen {
    override fun toString(): String {
        return "home"
    }
}

@Serializable
object ProfileScreen {
    override fun toString(): String {
        return "profile"
    }
}

@Serializable
object LoginScreen {
    override fun toString(): String {
        return "login"
    }
}

@Serializable
object SignUpScreen {
    override fun toString(): String {
        return "signup"
    }
}


@Serializable
data class ScreenB(
    val name: String?,
    val age: Int,
)
