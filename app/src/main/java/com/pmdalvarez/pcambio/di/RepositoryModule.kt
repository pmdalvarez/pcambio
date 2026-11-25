package com.pmdalvarez.pcambio.di

import com.pmdalvarez.pcambio.data.repository.OfflineFirstExchangeRatesRepository
import com.pmdalvarez.pcambio.domain.repository.ExchangeRatesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

  @Binds
  @Singleton
  abstract fun bindExchangeRatesRepository(
    repository: OfflineFirstExchangeRatesRepository,
  ): ExchangeRatesRepository
}