package com.ssafy.keywe.data.module

import com.ssafy.keywe.data.login.LoginDataSource
import com.ssafy.keywe.data.login.LoginRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class DataSourceModule {
    @Binds
    abstract fun bindLoginDataSource(loginRemoteDataSource: LoginRemoteDataSource): LoginDataSource
}