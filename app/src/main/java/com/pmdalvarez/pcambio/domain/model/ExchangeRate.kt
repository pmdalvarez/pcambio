package com.pmdalvarez.pcambio.domain.model

data class ExchangeRate(
  val currencyCode: String,
  val currencyName: String,
  val rateFromBaseCurrency: Double
)