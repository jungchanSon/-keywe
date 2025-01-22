package com.ssafy.keywe.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Inject

interface AuthService {
    fun login()
}

class AuthServiceImpl @Inject constructor() : AuthService {
    override fun login() {
        TODO("Not yet implemented")
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    fun provideAuthService(): AuthService {
        return Retrofit.Builder().baseUrl("").build().create(AuthService::class.java)
    }
}

//
//@EntryPoint
//@InstallIn(SingletonComponent::class)
