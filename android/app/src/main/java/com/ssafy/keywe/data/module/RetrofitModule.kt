package com.ssafy.keywe.data.module

import com.ssafy.keywe.data.KeyWeClient
import com.ssafy.keywe.data.login.LoginApiService
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
    fun provideLoginApiService(): LoginApiService = KeyWeClient.create(LoginApiService::class.java)
}