
package com.pmdalvarez.pcambio.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pmdalvarez.pcambio.R
import com.pmdalvarez.pcambio.ui.composable.CurrencySelector
import com.pmdalvarez.pcambio.ui.theme.PCambioPurple
import com.pmdalvarez.pcambio.ui.theme.PCambioPurpleDark
import com.pmdalvarez.pcambio.ui.theme.TextSecondary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterScreen(
  viewModel: CurrencyConverterViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  var baseCurrencyInput by rememberSaveable { mutableStateOf("") }
  var targetCurrencyInput by rememberSaveable { mutableStateOf("") }
  var baseCurrencyAmountInput by rememberSaveable { mutableStateOf("0.0") }

  // Target currency amount is updated when inputs or exchange rates change
  LaunchedEffect(
    baseCurrencyInput,
    targetCurrencyInput,
    baseCurrencyAmountInput,
    uiState.currencies
  ) {
    viewModel.updateTargetCurrencyAmount(
      baseCurrency = baseCurrencyInput,
      targetCurrency = targetCurrencyInput,
      baseCurrencyAmount = baseCurrencyAmountInput.toDoubleOrNull()
    )
  }

  Scaffold(
    topBar = {
      Column {
        TopAppBar(
          title = {
            Text(stringResource(R.string.app_name))
          },
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PCambioPurple,
            titleContentColor = Color.White
          )
        )

        HorizontalDivider(
          thickness = 1.dp,
          color = PCambioPurpleDark
        )
      }
    }
  ) { innerPadding ->

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .imePadding()
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Date of exchange rates + Base currency info
      Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        if (uiState.exchangeRatesDateFormatted.isNotBlank()) {
          Text(
            text = stringResource(R.string.exchange_rates_date,uiState.exchangeRatesDateFormatted),
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
          )
        }
        Text(
          text = stringResource(R.string.base_currency, uiState.baseCurrencyCode),
          style = MaterialTheme.typography.bodySmall,
          color = TextSecondary
        )
      }

      // Source currency
      CurrencySelector(
        label = stringResource(R.string.enter_source_currency),
        currencies = uiState.currencies,
        selectedCurrencyCode = baseCurrencyInput,
        onCurrencySelected = {
          baseCurrencyInput = it
        },
        modifier = Modifier.fillMaxWidth()
      )

      // Target currency
      CurrencySelector(
        label = stringResource(R.string.enter_target_currency),
        currencies = uiState.currencies,
        selectedCurrencyCode = targetCurrencyInput,
        onCurrencySelected = {
          targetCurrencyInput = it
        },
        modifier = Modifier.fillMaxWidth()
      )

      // Source amount
      OutlinedTextField(
        value = baseCurrencyAmountInput,
        onValueChange = {
          baseCurrencyAmountInput = it
        },
        label = {
          Text(stringResource(R.string.source_currency_amount))
        },
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Decimal
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
      )

      HorizontalDivider()

      uiState.targetCurrencyAmount?.let {
        Column(
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = stringResource(R.string.converted_amount),
            style = MaterialTheme.typography.titleMedium,
            color = TextSecondary
          )

          Text(
            text = String.format(
              Locale.getDefault(),
              "%f %s",
              it,
              targetCurrencyInput.uppercase()
            ),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
          )
        }      }

    }
  }
}
