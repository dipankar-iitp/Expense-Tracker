package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ExpenseViewModel
import com.example.ui.components.getCategoryColor
import com.example.ui.theme.BentoBlueContainer
import com.example.ui.theme.BentoBluePrimary
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoCardSecondary
import com.example.ui.theme.BentoDarkBlue
import com.example.ui.theme.BentoGreenContainer
import com.example.ui.theme.BentoGreenPrimary
import com.example.ui.theme.BentoRedAlert
import com.example.ui.theme.BentoRedAlertContainer
import com.example.ui.theme.BentoSurface
import com.example.ui.theme.BentoTextPrimary
import com.example.ui.theme.BentoTextSecondary

val expenseCategories = listOf(
  "Shopping", "Housing", "Food", "Utilities",
  "Entertainment", "Transport", "Health", "Salary", "Other"
)

val paymentMethods = listOf("Credit Card", "Cash", "Bank Transfer", "Apple / Google Pay")

@Composable
fun AddExpenseScreen(
  viewModel: ExpenseViewModel,
  onTransactionSaved: () -> Unit,
  modifier: Modifier = Modifier
) {
  var amountInput by remember { mutableStateOf("") }
  var titleInput by remember { mutableStateOf("") }
  var selectedType by remember { mutableStateOf("EXPENSE") } // "EXPENSE" or "INCOME"
  var selectedCategory by remember { mutableStateOf("Shopping") }
  var selectedPaymentMethod by remember { mutableStateOf("Credit Card") }
  var noteInput by remember { mutableStateOf("") }

  val categoryStatuses by viewModel.categoryBudgetStatuses.collectAsState()
  val scrollState = rememberScrollState()

  // Calculate if adding this expense exceeds category budget
  val amountVal = amountInput.toDoubleOrNull() ?: 0.0
  val currentCategoryStatus = categoryStatuses.find { it.category == selectedCategory }
  val currentSpent = currentCategoryStatus?.spent ?: 0.0
  val currentLimit = currentCategoryStatus?.limit ?: 500.0
  val willExceed = selectedType == "EXPENSE" && (currentSpent + amountVal) > currentLimit

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BentoSurface)
      .padding(horizontal = 20.dp)
      .verticalScroll(scrollState)
  ) {
    Spacer(modifier = Modifier.height(16.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
      Text(
        text = "Add Transaction",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = BentoDarkBlue,
        modifier = Modifier.weight(1f)
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Type Selector (Expense vs Income Bento Toggle)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(BentoCardSecondary)
        .padding(4.dp)
    ) {
      Surface(
        onClick = { selectedType = "EXPENSE" },
        shape = RoundedCornerShape(12.dp),
        color = if (selectedType == "EXPENSE") BentoRedAlert else Color.Transparent,
        modifier = Modifier.weight(1f)
      ) {
        Box(
          modifier = Modifier.padding(vertical = 12.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Expense",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (selectedType == "EXPENSE") BentoSurface else BentoTextSecondary
          )
        }
      }

      Surface(
        onClick = { selectedType = "INCOME" },
        shape = RoundedCornerShape(12.dp),
        color = if (selectedType == "INCOME") BentoGreenPrimary else Color.Transparent,
        modifier = Modifier.weight(1f)
      ) {
        Box(
          modifier = Modifier.padding(vertical = 12.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Income",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (selectedType == "INCOME") BentoSurface else BentoTextSecondary
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Streamlined Large Numeric Input Display
    Surface(
      shape = RoundedCornerShape(28.dp),
      color = if (selectedType == "EXPENSE") BentoRedAlertContainer.copy(alpha = 0.5f) else BentoGreenContainer.copy(alpha = 0.5f),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = if (selectedType == "EXPENSE") "EXPENSE AMOUNT" else "INCOME AMOUNT",
          style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.sp),
          fontWeight = FontWeight.Bold,
          color = if (selectedType == "EXPENSE") BentoRedAlert else BentoGreenPrimary
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "$ ",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = BentoDarkBlue
          )
          OutlinedTextField(
            value = amountInput,
            onValueChange = { input ->
              if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                amountInput = input
              }
            },
            placeholder = {
              Text("0.00", style = MaterialTheme.typography.displayMedium, color = BentoTextSecondary.copy(alpha = 0.4f))
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.displayMedium.copy(
              fontWeight = FontWeight.Bold,
              color = BentoDarkBlue,
              textAlign = TextAlign.Start
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Color.Transparent,
              unfocusedBorderColor = Color.Transparent,
              focusedContainerColor = Color.Transparent,
              unfocusedContainerColor = Color.Transparent
            ),
            modifier = Modifier.weight(1f)
          )
        }
      }
    }

    if (willExceed) {
      Spacer(modifier = Modifier.height(12.dp))
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = BentoRedAlertContainer,
        border = androidx.compose.foundation.BorderStroke(1.dp, BentoRedAlert)
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Warning",
            tint = BentoRedAlert,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Smart Alert: This $${String.format("%.2f", amountVal)} expense will exceed your $selectedCategory budget limit ($${String.format("%.0f", currentLimit)})!",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = BentoRedAlert
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Description Title Input
    OutlinedTextField(
      value = titleInput,
      onValueChange = { titleInput = it },
      label = { Text("Transaction Title / Description") },
      placeholder = { Text("e.g., Target Grocery Shopping") },
      singleLine = true,
      shape = RoundedCornerShape(16.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BentoBluePrimary,
        unfocusedBorderColor = BentoBorder
      ),
      modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(20.dp))

    // Category Selection
    Text(
      text = "Category",
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold,
      color = BentoDarkBlue
    )
    Spacer(modifier = Modifier.height(8.dp))

    LazyRow(
      horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      items(expenseCategories) { cat ->
        val isSelected = selectedCategory == cat
        val color = getCategoryColor(cat)

        Surface(
          onClick = { selectedCategory = cat },
          shape = RoundedCornerShape(16.dp),
          color = if (isSelected) color else BentoCardSecondary,
          border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, BentoBorder)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = getCategoryIcon(cat, selectedType),
              contentDescription = cat,
              tint = if (isSelected) BentoSurface else color,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = cat,
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) BentoSurface else BentoTextPrimary
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Payment Method Selection
    Text(
      text = "Payment Method",
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold,
      color = BentoDarkBlue
    )
    Spacer(modifier = Modifier.height(8.dp))

    LazyRow(
      horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      items(paymentMethods) { method ->
        val isSelected = selectedPaymentMethod == method
        Surface(
          onClick = { selectedPaymentMethod = method },
          shape = RoundedCornerShape(12.dp),
          color = if (isSelected) BentoDarkBlue else BentoCardSecondary
        ) {
          Text(
            text = method,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) BentoSurface else BentoTextSecondary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Note Input
    OutlinedTextField(
      value = noteInput,
      onValueChange = { noteInput = it },
      label = { Text("Note (Optional)") },
      placeholder = { Text("Add additional details...") },
      maxLines = 2,
      shape = RoundedCornerShape(16.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BentoBluePrimary,
        unfocusedBorderColor = BentoBorder
      ),
      modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(28.dp))

    // Save Button
    val isFormValid = amountVal > 0 && titleInput.isNotBlank()

    Button(
      onClick = {
        if (isFormValid) {
          viewModel.addExpense(
            title = titleInput.trim(),
            amount = amountVal,
            type = selectedType,
            category = selectedCategory,
            dateTimestamp = System.currentTimeMillis(),
            note = noteInput.trim(),
            paymentMethod = selectedPaymentMethod
          )
          onTransactionSaved()
        }
      },
      enabled = isFormValid,
      shape = RoundedCornerShape(20.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = if (selectedType == "EXPENSE") BentoBluePrimary else BentoGreenPrimary,
        disabledContainerColor = BentoBorder
      ),
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = "Save",
          tint = BentoSurface
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Save ${selectedType.lowercase().replaceFirstChar { it.uppercase() }}",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = BentoSurface
        )
      }
    }

    Spacer(modifier = Modifier.height(90.dp)) // bottom padding
  }
}
