package com.pmdalvarez.pcambio.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "exchange_rates")

class ExchangeRatesPreferences @Inject constructor(
  @ApplicationContext private val context: Context
) {
  val exchangeRatesDate: Flow<String?> = context.dataStore.data.map { preferences ->
    preferences[EXCHANGE_RATES_DATE]
  }

  suspend fun setExchangeRatesDate(date: String) {
    context.dataStore.edit { preferences ->
      preferences[EXCHANGE_RATES_DATE] = date
    }
  }

  private companion object {
    val EXCHANGE_RATES_DATE = stringPreferencesKey("exchange_rates_date")
  }
}