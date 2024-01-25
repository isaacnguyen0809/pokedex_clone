package com.isaac.pokedex_clone.di

import com.isaac.pokedex_clone.BuildConfig
import com.isaac.pokedex_clone.data.remote.AuthService
import com.isaac.pokedex_clone.data.remote.PokemonService
import com.isaac.pokedex_clone.data.remote.interceptor.AuthInterceptor
import com.isaac.pokedex_clone.data.remote.retrofit.ApiResponseCallAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseUrlQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthUrlQualifier

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @BaseUrlQualifier
    fun provideBaseUrl(): String = BuildConfig.API_DOMAIN

    @Provides
    @AuthUrlQualifier
    fun provideAuthUrl(): String = "http://10.0.2.2:3000/"

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Provides
    @BaseUrlQualifier
    @Singleton
    fun provideRetrofit(
        @BaseUrlQualifier baseUrl: String,
        moshi: Moshi,
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(ApiResponseCallAdapterFactory.create(moshi))
        .build()

    @Provides
    @AuthUrlQualifier
    @Singleton
    fun provideAuthRetrofit(
        @AuthUrlQualifier baseUrl: String,
        moshi: Moshi,
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun providePokemonService(@BaseUrlQualifier retrofit: Retrofit): PokemonService = retrofit.create()

    @Provides
    @Singleton
    fun provideAuthService(@AuthUrlQualifier retrofit: Retrofit): AuthService = retrofit.create()

}