package com.pmdalvarez.pcambio.domain.repository

interface ExchangeRatesRepository {

  suspend fun syncLatestExchageRates(): Boolean

  public companion object {
    const val BASE_CURRENCY = "usd"
  }

}