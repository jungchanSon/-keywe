package com.ssafy.keywe.core.di.module

import com.ssafy.keywe.data.auth.AuthDataSource
import com.ssafy.keywe.data.auth.AuthRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class DataSourceModule {
    @Binds
    abstract fun bindLoginDataSource(loginRemoteDataSource: AuthRemoteDataSource): AuthDataSource
}