
package com.pmdalvarez.pcambio.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmdalvarez.pcambio.domain.repository.ExchangeRatesRepository
import com.pmdalvarez.pcambio.domain.repository.ExchangeRatesRepository.Companion.BASE_CURRENCY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
  private val exchangeRatesRepository: ExchangeRatesRepository,
) : ViewModel() {

  private val _uiState = MutableStateFlow(
    CurrencyConverterUiState(
      baseCurrencyCode = BASE_CURRENCY.uppercase(Locale.ROOT)
    )
  )
  val uiState: StateFlow<CurrencyConverterUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      combine(
        exchangeRatesRepository.exchangeRates,
        exchangeRatesRepository.exchangeRatesDate
      ) { exchangeRates, date ->
        val currencies = exchangeRates.map {
          CurrencyUiModel(
            code = it.currencyCode,
            name = it.currencyName,
            rateFromBaseCurrency = it.rateFromBaseCurrency
          )
        }

        val formattedDate = date?.let {
          runCatching { formatDate(it) }.getOrDefault("")
        }.orEmpty()

        currencies to formattedDate
      }.collect { (currencies, formattedDate) ->
        _uiState.update {
          it.copy(
            currencies = currencies,
            exchangeRatesDateFormatted = formattedDate
          )
        }
      }
    }

    syncLatestExchangeRates()
  }

  fun updateTargetCurrencyAmount(
    baseCurrency: String?,
    targetCurrency: String?,
    baseCurrencyAmount: Double?
  ) {
    _uiState.update { currentState ->
      val baseRate = currentState.currencies.find {
        it.code.equals(baseCurrency, ignoreCase = true)
      }?.rateFromBaseCurrency

      val targetRate = currentState.currencies.find {
        it.code.equals(targetCurrency, ignoreCase = true)
      }?.rateFromBaseCurrency

      val targetAmount =
        if (baseCurrency.isNullOrBlank() ||
          targetCurrency.isNullOrBlank() ||
          baseCurrencyAmount == null ||
          baseRate == null || baseRate <= 0.0 ||
          targetRate == null
        ) {
          null
        } else {
          baseCurrencyAmount / baseRate * targetRate
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
    LocalDate.parse(date).format(
      DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
    )
}
