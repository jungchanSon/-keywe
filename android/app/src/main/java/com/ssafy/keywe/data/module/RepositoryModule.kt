package com.ssafy.keywe.data.module

import com.ssafy.keywe.data.login.LoginApiImplRepository
import com.ssafy.keywe.data.login.LoginApiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindLoginApiRepository(loginApiImplRepository: LoginApiImplRepository): LoginApiRepository
}