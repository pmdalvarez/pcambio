package com.pmdalvarez.pcambio.data.repository

import com.pmdalvarez.pcambio.data.local.CurrencyDao
import com.pmdalvarez.pcambio.data.local.CurrencyEntity
import com.pmdalvarez.pcambio.data.local.ExchangeRatesPreferences
import com.pmdalvarez.pcambio.data.remote.ExchangeRatesApi
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class OfflineFirstExchangeRatesRepository @Inject constructor(
  private val exchangeRatesApi: ExchangeRatesApi,
  private val currencyDao: CurrencyDao,
  private val exchangeRatesPreferences: ExchangeRatesPreferences
) {

  suspend fun syncLatestExchangeRates(): Boolean =
    try {
      // 1/3: Load latest currency names and exchange rates
      val currencyNames = exchangeRatesApi.getCurrencyNames()
      val exchangeRatesDto = exchangeRatesApi.getExchangeRates()

      // 2/:3 Store currency names and exchange rates in DB

      val currencyEntityList = mutableListOf<CurrencyEntity>()
      for ((currencyCode, exchangeRate) in exchangeRatesDto.rates) {
        exchangeRate?.let {
          currencyEntityList.add(
            CurrencyEntity(
              code = currencyCode,
              name = currencyNames[currencyCode] ?: "",
              rateFromBaseCurrency = it
            )
          )
        }
      }
      currencyDao.insertOrReplaceCurrencies(currencyEntityList)

      // 3/3: Store date of exchange rates in datastore

      exchangeRatesPreferences.setExchangeRatesDate(exchangeRatesDto.date)

      true
    } catch (exception: CancellationException) {
      throw exception
    } catch (_: Exception) {
      false
    }

}