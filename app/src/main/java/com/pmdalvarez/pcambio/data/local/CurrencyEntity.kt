package com.pmdalvarez.pcambio.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pmdalvarez.pcambio.domain.model.ExchangeRate

@Entity(tableName = "currency")
data class CurrencyEntity(
  @PrimaryKey
  val code: String,
  val name: String,
  val rateFromBaseCurrency: Double
)

fun CurrencyEntity.toExchangeRate(): ExchangeRate =
  ExchangeRate(
    currencyCode = code,
    currencyName = name,
    rateFromBaseCurrency = rateFromBaseCurrency
  )