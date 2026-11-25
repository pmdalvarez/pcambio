package com.pmdalvarez.pcambio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pmdalvarez.pcambio.ui.CurrencyConverterScreen
import com.pmdalvarez.pcambio.ui.theme.PCambioTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      PCambioTheme {
        CurrencyConverterScreen()
      }
    }
  }
}