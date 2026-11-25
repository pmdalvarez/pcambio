package com.pmdalvarez.pcambio.data.remote

import com.pmdalvarez.pcambio.domain.repository.ExchangeRatesRepository.Companion.BASE_CURRENCY
import kotlinx.serialization.SerialName

class CurrencyNamesDto(
  @SerialName(BASE_CURRENCY) val rates: Map<String, String>
)