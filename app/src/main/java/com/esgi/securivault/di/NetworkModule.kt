package com.esgi.securivault.di

import com.esgi.securivault.networking.RetrofitHttpClient
import com.esgi.securivault.networking.services.SuitcaseServices
import com.esgi.securivault.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return RetrofitHttpClient.createRetrofit {
            FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.result?.token
        }
    }

    @Provides
    @Singleton
    fun provideSuitcaseServices(retrofit: Retrofit): SuitcaseServices {
        return retrofit.create(SuitcaseServices::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(): LoginRepository {
        return LoginRepository()
    }
}