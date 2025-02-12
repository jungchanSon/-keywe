package com.ssafy.keywe.core.di.module

import com.ssafy.keywe.webrtc.ScreenSizeManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ScreenSizeModule {

    @Provides
    @Singleton
    fun provideScreenSizeManager(): ScreenSizeManager {
        return ScreenSizeManager()
    }
}