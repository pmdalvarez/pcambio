package com.pmdalvarez.pcambio.domain.repository

import com.pmdalvarez.pcambio.domain.model.ExchangeRate
import kotlinx.coroutines.flow.Flow

interface ExchangeRatesRepository {

  val exchangeRates: Flow<List<ExchangeRate>>
  suspend fun syncLatestExchangeRates(): Boolean

  companion object {
    const val BASE_CURRENCY = "usd"
  }

}