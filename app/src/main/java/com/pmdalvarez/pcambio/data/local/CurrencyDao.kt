package com.pmdalvarez.pcambio.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrReplaceCurrencies(currencies: List<CurrencyEntity>)

  @Query("SELECT * FROM currency ORDER BY name ASC")
  fun getAllCurrencies(): Flow<List<CurrencyEntity>>
}