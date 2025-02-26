package com.example.randomuserapp.di

import android.app.Application
import com.example.randomuserapp.data.api.RandomUserApi
import com.example.randomuserapp.data.repository.UserRepositoryImpl
import com.example.randomuserapp.domain.repository.UserRepository
import com.example.randomuserapp.domain.usecase.GetUsersUseCase
import com.example.randomuserapp.utils.NetworkUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideRandomUserApi(): RandomUserApi {
        return Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RandomUserApi::class.java)
    }

    @Provides
    fun provideUserRepository(api: RandomUserApi): UserRepository {
        return UserRepositoryImpl(api)
    }

    @Provides
    fun provideGetUsersUseCase(repository: UserRepository): GetUsersUseCase {
        return GetUsersUseCase(repository)
    }

    @Provides
    fun provideNetworkUtil(application: Application): NetworkUtil {
        return NetworkUtil(application)
    }
}
