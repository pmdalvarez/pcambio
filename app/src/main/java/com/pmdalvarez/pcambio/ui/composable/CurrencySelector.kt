
package com.pmdalvarez.pcambio.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.pmdalvarez.pcambio.ui.CurrencyUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencySelector(
  label: String,
  currencies: List<CurrencyUiModel>,
  selectedCurrencyCode: String,
  onCurrencySelected: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var expanded by remember { mutableStateOf(false) }
  var query by remember { mutableStateOf("") }

  val selectedCurrency = currencies.find { it.code.equals(selectedCurrencyCode, ignoreCase = true) }

  val filteredCurrencies = currencies.filter {
    it.name.contains(query, ignoreCase = true) || it.code.contains(query, ignoreCase = true)
  }

  ExposedDropdownMenuBox(
    expanded = expanded,
    onExpandedChange = {
      expanded = it
      if (it) query = ""
    },
    modifier = modifier
  ) {
    OutlinedTextField(
      value = if (expanded) {
        query
      } else {
        selectedCurrency?.let { "${it.name} (${it.code.uppercase()})" } ?: ""
      },
      onValueChange = {
        query = it
        expanded = true
      },
      label = { Text(label) },
      trailingIcon = {
        ExposedDropdownMenuDefaults.TrailingIcon(expanded)
      },
      singleLine = true,
      modifier = Modifier
        .fillMaxWidth()
        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
    )

    ExposedDropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false }
    ) {
      filteredCurrencies.forEach { currency ->
        DropdownMenuItem(
          text = {
            Text("${currency.name} (${currency.code.uppercase()})")
          },
          onClick = {
            onCurrencySelected(currency.code)
            expanded = false
          }
        )
      }
    }
  }
}