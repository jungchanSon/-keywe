//package com.ssafy.keywe.data.module
//
//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.core.DataStoreFactory
//import androidx.datastore.dataStoreFile
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@InstallIn(SingletonComponent::class)
//@Module
//class DataStoreModule {
//
//    @Provides
//    @Singleton
//    fun provideProtoDataStore(
//        @ApplicationContext context: Context,
//    ): DataStore<TokenProto.Token> {
//        return DataStoreFactory.create(
//            serializer = com.ssafy.keywe.data.datasource.TokenSerializer(),
//            produceFile = { context.dataStoreFile("token.pb") }
//        )
//    }
//}