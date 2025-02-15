package com.ssafy.keywe.core.di.module

import com.ssafy.keywe.data.AuthAuthenticator
import com.ssafy.keywe.data.AuthInterceptor
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.webrtc.KeyWeWebSocket
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RestApiClientQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WebSocketClientQualifier

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideSignalWebSocket(
        @WebSocketClientQualifier okHttpClient: OkHttpClient,
    ): KeyWeWebSocket {
        return KeyWeWebSocket(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor {
        return AuthInterceptor(tokenManager)
    }

    @Provides
    @Singleton
    fun provideAuthAuthenticator(
        tokenManager: TokenManager,
    ): AuthAuthenticator {
        return AuthAuthenticator(tokenManager)
    }

    @Provides
    @Singleton
    @RestApiClientQualifier
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator,
    ): OkHttpClient {
        val provideLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder().addInterceptor(authInterceptor) // Add AuthInterceptor
            .authenticator(authAuthenticator) // Add AuthAuthenticator
            .addInterceptor(provideLoggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS) // 연결 타임아웃 설정
            .readTimeout(30, TimeUnit.SECONDS) // 읽기 타임아웃 설정
            .writeTimeout(30, TimeUnit.SECONDS).build()
    }

    @Provides
    @Singleton
    @WebSocketClientQualifier
    fun provideWebSocketClient(): OkHttpClient {
        val provideLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            // 웹 소켓 연결 유지
            .callTimeout(1, TimeUnit.MINUTES).pingInterval(10, TimeUnit.SECONDS)
            .addInterceptor(provideLoggingInterceptor).build()
    }

}
