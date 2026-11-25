package com.pmdalvarez.pcambio.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class CurrencyEntity(
  @PrimaryKey
  val code: String,
  val name: String,
  val rateFromBaseCurrency: Double
)