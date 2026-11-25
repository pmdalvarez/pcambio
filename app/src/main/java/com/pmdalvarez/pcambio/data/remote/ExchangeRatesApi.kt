package com.pmdalvarez.pcambio.data.remote

import com.pmdalvarez.pcambio.domain.repository.ExchangeRatesRepository.Companion.BASE_CURRENCY
import retrofit2.http.GET

interface ExchangeRatesApi {

  @GET("v1/currencies.min.json")
  suspend fun getCurrencyNames(): Map<String, String>

  @GET("v1/currencies/"+ BASE_CURRENCY +".min.json")
  suspend fun getExchangeRates(): ExchangeRatesDto

}