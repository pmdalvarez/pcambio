package com.pmdalvarez.pcambio.ui

data class CurrencyConverterUiState(
  val currencies: List<CurrencyUiModel> = emptyList(),
  val exchangeRatesDateFormatted: String = "",
  val baseCurrencyCode: String = "",
  val targetCurrencyAmount: Double? = null
)