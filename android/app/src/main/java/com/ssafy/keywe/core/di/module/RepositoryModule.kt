package com.ssafy.keywe.core.di.module

import com.ssafy.keywe.data.auth.AuthRepositoryImpl
import com.ssafy.keywe.data.fcm.FCMRepositoryImpl
import com.ssafy.keywe.data.order.OrderRepositoryImpl
import com.ssafy.keywe.data.profile.ProfileRepositoryImpl
import com.ssafy.keywe.domain.auth.AuthRepository
import com.ssafy.keywe.domain.fcm.FCMRepository
import com.ssafy.keywe.domain.order.OrderRepository
import com.ssafy.keywe.domain.profile.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLoginRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(orderRepositoryImpl: OrderRepositoryImpl): OrderRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindFCMRepository(fcmRepositoryImpl: FCMRepositoryImpl): FCMRepository

}