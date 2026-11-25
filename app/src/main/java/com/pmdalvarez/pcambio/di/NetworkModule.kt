package com.pmdalvarez.pcambio.di

import com.pmdalvarez.pcambio.data.remote.ExchangeRatesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  private const val BASE_URL = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/"

  @Provides
  @Singleton
  fun provideJson(): Json =
    Json {
      ignoreUnknownKeys = true
      namingStrategy = JsonNamingStrategy.SnakeCase
    }

  @Provides
  @Singleton
  fun provideApiKeyInterceptor(): Interceptor =
    Interceptor { chain ->
      val request = chain.request()

      val url = request.url
        .newBuilder()
        .build()

      chain.proceed(
        request.newBuilder()
          .url(url)
          .build(),
      )
    }

  @Provides
  @Singleton
  fun provideOkHttpClient(
    apiKeyInterceptor: Interceptor
  ): OkHttpClient =
    OkHttpClient.Builder()
      .addInterceptor(apiKeyInterceptor)
      .build()

  @Provides
  @Singleton
  fun provideRetrofit(
    json: Json,
    okHttpClient: OkHttpClient,
  ): Retrofit =
    Retrofit.Builder()
      .baseUrl(BASE_URL)
      .client(okHttpClient)
      .addConverterFactory(
        json.asConverterFactory(
          "application/json".toMediaType(),
        ),
      )
      .build()

  @Provides
  @Singleton
  fun provideExchangeRatesApi(
    retrofit: Retrofit,
  ): ExchangeRatesApi =
    retrofit.create(ExchangeRatesApi::class.java)
}