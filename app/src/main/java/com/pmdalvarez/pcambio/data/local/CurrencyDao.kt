package com.pmdalvarez.pcambio.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface CurrencyDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrReplaceCurrencies(currencies: List<CurrencyEntity>)
}