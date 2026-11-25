package com.pmdalvarez.pcambio.di

import android.content.Context
import androidx.room.Room
import com.pmdalvarez.pcambio.data.local.CurrencyDao
import com.pmdalvarez.pcambio.data.local.PCambioDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

  @Provides
  @Singleton
  fun provideDatabase(
    @ApplicationContext context: Context
  ): PCambioDatabase =
    Room.databaseBuilder(
      context,
      PCambioDatabase::class.java,
      "pcambio.db"
    ).build()

  @Provides
  fun provideCurrencyDao(
    database: PCambioDatabase
  ): CurrencyDao = database.currencyDao()
}