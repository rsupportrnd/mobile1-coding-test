package com.rsupport.mobile1.test.di

import com.rsupport.mobile1.test.network.GettyService
import com.rsupport.mobile1.test.repository.GettyRepository
import com.rsupport.mobile1.test.repository.GettyRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideGettyService(
    ) : GettyService = GettyService()

    @Singleton
    @Provides
    fun provideGettyRepository(
        gettyService: GettyService
    ) : GettyRepository = GettyRepositoryImpl(gettyService = gettyService)
}