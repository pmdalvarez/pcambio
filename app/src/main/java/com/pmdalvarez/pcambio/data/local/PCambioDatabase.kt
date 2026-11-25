package com.pmdalvarez.pcambio.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
  entities = [
    CurrencyEntity::class
  ],
  version = 1,
  exportSchema = false,
)
abstract class PCambioDatabase : RoomDatabase() {

}