package com.santucci.searchappdesafio.di

import android.content.Context
import com.google.gson.Gson
import com.santucci.searchappdesafio.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideProductRepository(
        @ApplicationContext context: Context,
        gson: Gson
    ): ProductRepository {
        return ProductRepository(context, gson)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }
}
