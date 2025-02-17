//package com.ssafy.keywe.core.di.module
//
//
//import com.google.gson.Gson
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class) // ✅ 앱 전체에서 사용 가능하도록 Singleton 범위로 설정
//object AppModule {
//
//    @Provides
//    @Singleton
//    fun provideGson(): Gson {
//        return Gson()
//    }
//}
