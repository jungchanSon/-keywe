package com.ssafy.keywe.data.module

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ssafy.keywe.data.auth.NetworkUtil
import com.ssafy.keywe.data.auth.NetworkUtil.jsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(NetworkUtil.BASE_URL).client(okHttpClient)
            .addConverterFactory(
                jsonBuilder.asConverterFactory("application/json".toMediaType())
            ).build()
    }
}
