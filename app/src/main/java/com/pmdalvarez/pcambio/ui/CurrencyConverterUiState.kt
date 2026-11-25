package com.pmdalvarez.pcambio.ui

data class CurrencyConverterUiState(
  val currencies: List<CurrencyUiModel> = listOf(),
  val exchangeRatesDateFormatted: String = "",
  val targetCurrencyAmount: Double = 0.0,
)