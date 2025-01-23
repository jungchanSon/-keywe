package com.ssafy.keywe.data.module

import com.ssafy.keywe.data.login.LoginRepository
import com.ssafy.keywe.data.login.LoginRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindLoginApiRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository
}