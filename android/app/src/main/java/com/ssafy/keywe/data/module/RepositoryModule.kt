package com.ssafy.keywe.data.module

import com.ssafy.keywe.data.auth.AuthRepository
import com.ssafy.keywe.data.auth.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindLoginApiRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}