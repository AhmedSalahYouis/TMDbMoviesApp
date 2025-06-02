package com.example.tmdbmoviesapp.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private val Context.dataStore by preferencesDataStore(name = "settings")

    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context) = context.dataStore
}
