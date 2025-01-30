package com.ssafy.keywe.util

import android.util.Log
import com.auth0.android.jwt.JWT

object JWTUtil {
    fun isTempToken(token: String): Boolean {
        val jwt = JWT(token)

        val issuer = jwt.issuer //get registered claims
        val claim = jwt.getClaim("TYPE").asString() //get custom claims
        val isExpired = jwt.isExpired(10) // Do time validation with 10 seconds leeway
        Log.d("jwt", "issuer = $issuer, claim = $claim, isExpired = $isExpired")
        return claim == "TEMP"
    }


}
