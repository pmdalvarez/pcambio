package com.pmdalvarez.pcambio.domain.repository

interface ExchangeRatesRepository {

  suspend fun syncLatestExchangeRates(): Boolean

  companion object {
    const val BASE_CURRENCY = "usd"
  }

}