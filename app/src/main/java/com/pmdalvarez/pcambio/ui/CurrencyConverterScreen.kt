package com.pmdalvarez.pcambio.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pmdalvarez.pcambio.R
import com.pmdalvarez.pcambio.ui.theme.PCambioPurpleDark

@Composable
fun CurrencyConverterScreen(
  viewModel: CurrencyConverterViewModel = hiltViewModel()
) {
  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .background(PCambioPurpleDark)
      .windowInsetsPadding(WindowInsets.statusBars),
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
    ) {
      Text(
        text = stringResource(R.string.app_name),
        style = MaterialTheme.typography.bodyLarge,
      )
    }
  }
}