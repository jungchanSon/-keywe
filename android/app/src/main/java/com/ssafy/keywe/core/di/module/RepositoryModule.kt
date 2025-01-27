package com.ssafy.keywe.core.di.module

import com.ssafy.keywe.data.auth.AuthRepositoryImpl
import com.ssafy.keywe.domain.auth.AuthRepository
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