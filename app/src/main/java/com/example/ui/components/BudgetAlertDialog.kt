package com.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BentoBluePrimary
import com.example.ui.theme.BentoDarkBlue
import com.example.ui.theme.BentoSurface
import com.example.ui.theme.BentoTextSecondary

@Composable
fun BudgetAlertDialog(
  category: String,
  currentLimit: Double,
  onDismiss: () -> Unit,
  onSave: (Double) -> Unit
) {
  var limitInput by remember { mutableStateOf(currentLimit.toInt().toString()) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Set Monthly Budget: $category",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = BentoDarkBlue
      )
    },
    text = {
      Column {
        Text(
          text = "Enter your desired monthly limit for this category. Smart alerts will notify you if spending exceeds this limit.",
          style = MaterialTheme.typography.bodyMedium,
          color = BentoTextSecondary
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
          value = limitInput,
          onValueChange = { input ->
            if (input.all { it.isDigit() }) {
              limitInput = input
            }
          },
          label = { Text("Monthly Limit ($)") },
          prefix = { Text("$ ") },
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val valDouble = limitInput.toDoubleOrNull() ?: currentLimit
          onSave(valDouble)
        },
        colors = ButtonDefaults.buttonColors(containerColor = BentoBluePrimary)
      ) {
        Text("Save Limit", color = BentoSurface, fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel", color = BentoTextSecondary)
      }
    },
    containerColor = BentoSurface,
    shape = RoundedCornerShape(24.dp)
  )
}
