package com.esgi.securivault.di

import com.esgi.securivault.networking.RetrofitHttpClient
import com.esgi.securivault.networking.services.SuitcaseServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideSuitcaseServices(): SuitcaseServices {
        return RetrofitHttpClient.loginRetrofit.create(SuitcaseServices::class.java)
    }
}