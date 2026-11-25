package com.pmdalvarez.pcambio.data.remote

import com.pmdalvarez.pcambio.domain.repository.ExchangeRatesRepository.Companion.BASE_CURRENCY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ExchangeRatesDto(
  val date: String,
  // Double? allows null rates to be accepted and rest of list to be parsed
  @SerialName(BASE_CURRENCY) val rates: Map<String, Double?>
)