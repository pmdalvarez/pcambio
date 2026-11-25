package com.pmdalvarez.pcambio.data.repository

import com.pmdalvarez.pcambio.data.local.CurrencyDao
import com.pmdalvarez.pcambio.data.local.ExchangeRatesPreferences
import com.pmdalvarez.pcambio.data.remote.ExchangeRatesApi
import com.pmdalvarez.pcambio.data.remote.ExchangeRatesDto
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.assertFailsWith

class OfflineFirstExchangeRatesRepositoryTest {

  private val exchangeRatesApi = mockk<ExchangeRatesApi>()
  private val currencyDao = mockk<CurrencyDao>()
  private val exchangeRatesPreferences = mockk<ExchangeRatesPreferences>()

  @Test
  fun `GIVEN WHEN syncLatestExchangeRates is called THEN true is returned`() = runTest {
    // Given
    val currencyNames = mapOf(
      "usd" to "US Dollar",
      "eur" to "Euro"
    )
    val exchangeRatesDto = ExchangeRatesDto(
      date = "2026-11-25",
      rates = mapOf(
        "usd" to 1.0,
        "eur" to 0.86
      )
    )

    every { currencyDao.getAllCurrencies() } returns flowOf(emptyList())
    every { exchangeRatesPreferences.exchangeRatesDate } returns flowOf(null)

    coEvery { exchangeRatesApi.getCurrencyNames() } returns currencyNames
    coEvery { exchangeRatesApi.getExchangeRates() } returns exchangeRatesDto
    coEvery { currencyDao.insertOrReplaceCurrencies(any()) } returns Unit
    coEvery { exchangeRatesPreferences.setExchangeRatesDate(any()) } returns Unit

    val repository = OfflineFirstExchangeRatesRepository(
      exchangeRatesApi,
      currencyDao,
      exchangeRatesPreferences
    )

    // When
    val result = repository.syncLatestExchangeRates()

    // Then
    assertTrue(result)
  }

  @Test
  fun `GIVEN getExchangeRates throws an exception WHEN syncLatestExchangeRates is called THEN false is returned`() = runTest {
    // Given
    every { currencyDao.getAllCurrencies() } returns flowOf(emptyList())
    every { exchangeRatesPreferences.exchangeRatesDate } returns flowOf(null)

    coEvery { exchangeRatesApi.getCurrencyNames() } returns mapOf(
      "usd" to "US Dollar"
    )
    coEvery { exchangeRatesApi.getExchangeRates() } throws RuntimeException("API error")

    val repository = OfflineFirstExchangeRatesRepository(
      exchangeRatesApi,
      currencyDao,
      exchangeRatesPreferences
    )

    // When
    val result = repository.syncLatestExchangeRates()

    // Then
    assertFalse(result)
  }

  @Test
  fun `GIVEN getAllCurrencies throws an exception WHEN syncLatestExchangeRates is called THEN false is returned`() = runTest {
    // Given
    every { currencyDao.getAllCurrencies() } returns flowOf(emptyList())
    every { exchangeRatesPreferences.exchangeRatesDate } returns flowOf(null)

    coEvery { exchangeRatesApi.getCurrencyNames() } throws RuntimeException("API error")
    coEvery { exchangeRatesApi.getExchangeRates() } returns ExchangeRatesDto(
      date = "2026-11-25",
      rates = mapOf(
        "usd" to 1.0,
        "eur" to 0.86
      )
    )

    val repository = OfflineFirstExchangeRatesRepository(
      exchangeRatesApi,
      currencyDao,
      exchangeRatesPreferences
    )

    // When
    val result = repository.syncLatestExchangeRates()

    // Then
    assertFalse(result)
  }

  @Test
  fun `GIVEN insertOrReplaceCurrencies throws an exception WHEN syncLatestExchangeRates is called THEN false is returned`() = runTest {
  // Given
    val currencyNames = mapOf(
      "usd" to "US Dollar",
      "eur" to "Euro"
    )
    val exchangeRatesDto = ExchangeRatesDto(
      date = "2026-11-25",
      rates = mapOf(
        "usd" to 1.0,
        "eur" to 0.86
      )
    )

    every { currencyDao.getAllCurrencies() } returns flowOf(emptyList())
    every { exchangeRatesPreferences.exchangeRatesDate } returns flowOf(null)

    coEvery { exchangeRatesApi.getCurrencyNames() } returns currencyNames
    coEvery { exchangeRatesApi.getExchangeRates() } returns exchangeRatesDto
    coEvery { currencyDao.insertOrReplaceCurrencies(any()) } throws RuntimeException("DB error")
    coEvery { exchangeRatesPreferences.setExchangeRatesDate(any()) } returns Unit

    val repository = OfflineFirstExchangeRatesRepository(
      exchangeRatesApi,
      currencyDao,
      exchangeRatesPreferences
    )

    // When
    val result = repository.syncLatestExchangeRates()

    // Then
    assertFalse(result)
  }

  @Test
  fun `GIVEN CancellationException is thrown WHEN syncLatestExchangeRates is called THEN exception is rethrown`() = runTest {
    // Given
    every { currencyDao.getAllCurrencies() } returns flowOf(emptyList())
    every { exchangeRatesPreferences.exchangeRatesDate } returns flowOf(null)

    coEvery { exchangeRatesApi.getCurrencyNames() } returns mapOf(
      "usd" to "US Dollar"
    )
    coEvery { exchangeRatesApi.getExchangeRates() } throws
        CancellationException("Cancelled")

    val repository = OfflineFirstExchangeRatesRepository(
      exchangeRatesApi,
      currencyDao,
      exchangeRatesPreferences
    )

    // When / Then
    assertFailsWith<CancellationException> {
      repository.syncLatestExchangeRates()
    }
  }

}