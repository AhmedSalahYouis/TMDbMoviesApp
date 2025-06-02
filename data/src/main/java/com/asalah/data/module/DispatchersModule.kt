package com.asalah.data.module

import com.asalah.data.util.DiskExecutor
import com.asalah.data.util.DispatchersProvider
import com.asalah.data.util.DispatchersProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class DispatchersModule {

    @Provides
    fun provideDiskExecutor(): DiskExecutor {
        return DiskExecutor()
    }

    @Provides
    fun provideDispatchersProvider(): DispatchersProvider {
        return DispatchersProviderImpl
    }

}