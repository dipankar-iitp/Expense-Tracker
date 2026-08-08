package com.example.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.CategoryBudgetStatus
import com.example.ui.ExpenseViewModel
import com.example.ui.components.BudgetAlertDialog
import com.example.ui.components.CategoryProgressBar
import com.example.ui.components.DonutChart
import com.example.ui.components.SmartBudgetAlertCard
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoDarkBlue
import com.example.ui.theme.BentoGreenPrimary
import com.example.ui.theme.BentoRedAlert
import com.example.ui.theme.BentoSurface
import com.example.ui.theme.BentoTextSecondary

@Composable
fun SpendingInsightsScreen(
  viewModel: ExpenseViewModel,
  initialCategoryToEdit: String? = null,
  modifier: Modifier = Modifier
) {
  val categoryStatuses by viewModel.categoryBudgetStatuses.collectAsState()
  val alerts by viewModel.exceededAlerts.collectAsState()
  val totalExpense by viewModel.totalExpense.collectAsState()

  var editingCategoryStatus by remember {
    mutableStateOf<CategoryBudgetStatus?>(
      categoryStatuses.find { it.category == initialCategoryToEdit }
    )
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(16.dp))
      Text(
        text = "Spending Insights",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = BentoDarkBlue
      )
      Text(
        text = "Visual data analytics & category budget tracking",
        style = MaterialTheme.typography.bodyMedium,
        color = BentoTextSecondary
      )
    }

    if (alerts.isNotEmpty()) {
      item {
        SmartBudgetAlertCard(
          alerts = alerts,
          onAdjustBudgetClick = { category ->
            editingCategoryStatus = categoryStatuses.find { it.category == category }
          }
        )
      }
    }

    item {
      Surface(
        shape = RoundedCornerShape(28.dp),
        color = BentoSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Category Distribution",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = BentoDarkBlue
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          DonutChart(statuses = categoryStatuses)
        }
      }
    }

    item {
      Text(
        text = "Category Budget Bars",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = BentoDarkBlue,
        modifier = Modifier.padding(top = 8.dp)
      )
    }

    items(categoryStatuses, key = { it.category }) { status ->
      CategoryProgressBar(
        status = status,
        onEditBudget = { editingCategoryStatus = status }
      )
    }

    item {
      Spacer(modifier = Modifier.height(80.dp))
    }
  }

  editingCategoryStatus?.let { status ->
    BudgetAlertDialog(
      category = status.category,
      currentLimit = status.limit,
      onDismiss = { editingCategoryStatus = null },
      onSave = { newLimit ->
        viewModel.updateBudget(status.category, newLimit)
        editingCategoryStatus = null
      }
    )
  }
}
