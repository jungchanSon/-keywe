package com.ssafy.keywe.data.module

import com.ssafy.keywe.data.KeyWeClient
import com.ssafy.keywe.data.login.LoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {
    @Singleton
    @Provides
    fun provideLoginApiService(): LoginService = KeyWeClient.create(LoginService::class.java)
}