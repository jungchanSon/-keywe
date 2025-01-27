package com.ssafy.keywe.core.di.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.ssafy.keywe.TokenProto
import com.ssafy.keywe.data.TokenSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun providesTokenDataStore(
        @ApplicationContext context: Context,
    ): DataStore<TokenProto> = DataStoreFactory.create(
        serializer = TokenSerializer, produceFile = {
            context.dataStoreFile("token.pb")
        }, scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    )
}