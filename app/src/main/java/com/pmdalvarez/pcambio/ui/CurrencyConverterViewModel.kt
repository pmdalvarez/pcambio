package com.pmdalvarez.pcambio.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmdalvarez.pcambio.domain.repository.ExchangeRatesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
  private val exchangeRatesRepository: ExchangeRatesRepository,
)  : ViewModel() {

  init {
    syncLatestExchangesRates()
  }


  fun syncLatestExchangesRates() {
    viewModelScope.launch {
      val result = exchangeRatesRepository.syncLatestExchageRates()
      // TODO - If result is false then show snackbar
    }
  }

}