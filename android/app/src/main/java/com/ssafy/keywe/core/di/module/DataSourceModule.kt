package com.ssafy.keywe.core.di.module

//import com.ssafy.keywe.data.auth.AuthDataSourceImpl
import com.ssafy.keywe.data.auth.AuthDataSource
import com.ssafy.keywe.data.auth.AuthRemoteDataSource
import com.ssafy.keywe.data.fcm.FCMDataSource
import com.ssafy.keywe.data.fcm.FCMRemoteDataSource
import com.ssafy.keywe.data.order.OrderDataSource
import com.ssafy.keywe.data.order.OrderRemoteDataSource
import com.ssafy.keywe.data.profile.ProfileDataSource
import com.ssafy.keywe.data.profile.ProfileRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindLoginDataSource(loginRemoteDataSource: AuthRemoteDataSource): AuthDataSource

    @Binds
    @Singleton
    abstract fun bindOrderDataSource(orderRemoteDataSource: OrderRemoteDataSource): OrderDataSource

    @Binds
    @Singleton
    abstract fun bindProfileDataSource(profileRemoteDataSource: ProfileRemoteDataSource): ProfileDataSource

    @Binds
    @Singleton
    abstract fun bindFCMDataSource(fcmRemoteDataSource: FCMRemoteDataSource): FCMDataSource


}