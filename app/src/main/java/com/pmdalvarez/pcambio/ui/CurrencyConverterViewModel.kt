
package com.pmdalvarez.pcambio.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmdalvarez.pcambio.domain.repository.ExchangeRatesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
  private val exchangeRatesRepository: ExchangeRatesRepository,
) : ViewModel() {

  private val _uiState = MutableStateFlow(CurrencyConverterUiState())
  val uiState: StateFlow<CurrencyConverterUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      combine(
        exchangeRatesRepository.exchangeRates,
        exchangeRatesRepository.exchangeRatesDate
      ) { exchangeRates, date ->
        CurrencyConverterUiState(
          currencies = exchangeRates.map {
            CurrencyUiModel(
              code = it.currencyCode,
              name = it.currencyName,
              rateFromBaseCurrency = it.rateFromBaseCurrency
            )
          },
          exchangeRatesDateFormatted = date?.let(::formatDate).orEmpty()
        )
      }.collect { updatedState ->
        _uiState.update { currentState ->
          currentState.copy(
            currencies = updatedState.currencies,
            exchangeRatesDateFormatted = updatedState.exchangeRatesDateFormatted
          )
        }
      }
    }

    syncLatestExchangeRates()
  }

  fun updateTargetCurrencyAmount(
    baseCurrency: String,
    targetCurrency: String,
    baseCurrencyAmount: Double
  ) {
    _uiState.update { currentState ->
      val baseRate = currentState.currencies.find {
        it.code.equals(baseCurrency, ignoreCase = true)
      }?.rateFromBaseCurrency

      val targetRate = currentState.currencies.find {
        it.code.equals(targetCurrency, ignoreCase = true)
      }?.rateFromBaseCurrency

      val targetAmount =
        if (baseRate != null && baseRate > 0.0 && targetRate != null) {
          baseCurrencyAmount / baseRate * targetRate
        } else {
          0.0
        }

      currentState.copy(targetCurrencyAmount = targetAmount)
    }
  }

  fun syncLatestExchangeRates() {
    viewModelScope.launch {
      val success = exchangeRatesRepository.syncLatestExchangeRates()
      // TODO: Show a snackbar if synchronization fails.
    }
  }

  private fun formatDate(date: String): String =
    try {
      LocalDate.parse(date).format(
        DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH)
      )
    } catch (_: DateTimeParseException) {
      ""
    }
}
