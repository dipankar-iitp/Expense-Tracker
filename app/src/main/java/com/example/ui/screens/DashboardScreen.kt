package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ExpenseEntity
import com.example.ui.ExpenseViewModel
import com.example.ui.components.BentoGridRow
import com.example.ui.components.BentoHeader
import com.example.ui.components.HeroBalanceBentoCard
import com.example.ui.components.SmartBudgetAlertCard
import com.example.ui.components.SpendingLineChart
import com.example.ui.components.getCategoryColor
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
  viewModel: ExpenseViewModel,
  onSeeAllClick: () -> Unit,
  onAdjustBudgetClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val totalBalance by viewModel.totalBalance.collectAsState()
  val totalIncome by viewModel.totalIncome.collectAsState()
  val categoryStatuses by viewModel.categoryBudgetStatuses.collectAsState()
  val alerts by viewModel.exceededAlerts.collectAsState()
  val trendPoints by viewModel.spendingTrendPoints.collectAsState()
  val allExpenses by viewModel.allExpenses.collectAsState()

  val recentExpenses = allExpenses.take(4)
  val topShopping = categoryStatuses.find { it.category == "Shopping" } ?: categoryStatuses.firstOrNull()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
  ) {
    item {
      BentoHeader(userName = "Alex Rivera")
    }

    if (alerts.isNotEmpty()) {
      item {
        SmartBudgetAlertCard(
          alerts = alerts,
          onAdjustBudgetClick = onAdjustBudgetClick
        )
      }
    }

    item {
      HeroBalanceBentoCard(
        totalBalance = totalBalance,
        monthlyTrendPct = "+12% net growth"
      )
    }

    item {
      BentoGridRow(
        topCategory = topShopping,
        totalSavings = totalIncome,
        onShoppingClick = onSeeAllClick,
        onSavingsClick = onSeeAllClick
      )
    }

    item {
      Surface(
        shape = RoundedCornerShape(28.dp),
        color = BentoSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          SpendingLineChart(points = trendPoints)
        }
      }
    }

    item {
      Surface(
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp, bottomStart = 24.dp, bottomEnd = 24.dp),
        color = BentoSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Recent Transactions",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = BentoDarkBlue
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onSeeAllClick) {
              Text(
                text = "See all",
                style = MaterialTheme.typography.titleSmall,
                color = BentoBluePrimary,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          if (recentExpenses.isEmpty()) {
            Text(
              text = "No recent transactions found.",
              style = MaterialTheme.typography.bodyMedium,
              color = BentoTextSecondary,
              modifier = Modifier.padding(vertical = 16.dp)
            )
          } else {
            Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)) {
              recentExpenses.forEach { expense ->
                TransactionItemRow(expense = expense)
              }
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(80.dp)) // bottom padding for nav bar
    }
  }
}

@Composable
fun TransactionItemRow(
  expense: ExpenseEntity,
  onDeleteClick: (() -> Unit)? = null
) {
  val isIncome = expense.type == "INCOME"
  val categoryColor = getCategoryColor(expense.category)
  val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
  val dateStr = dateFormat.format(Date(expense.dateTimestamp))

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(48.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(if (isIncome) BentoGreenContainer else categoryColor.copy(alpha = 0.15f)),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = getCategoryIcon(expense.category, expense.type),
        contentDescription = expense.category,
        tint = if (isIncome) BentoGreenPrimary else categoryColor,
        modifier = Modifier.size(24.dp)
      )
    }

    Spacer(modifier = Modifier.width(14.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = expense.title,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        color = BentoTextPrimary
      )
      Text(
        text = "$dateStr • ${expense.category}",
        style = MaterialTheme.typography.bodySmall,
        color = BentoTextSecondary
      )
    }

    Column(horizontalAlignment = Alignment.End) {
      Text(
        text = "${if (isIncome) "+" else "-"}$${String.format("%,.2f", expense.amount)}",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = if (isIncome) BentoGreenPrimary else BentoRedAlert
      )
      Text(
        text = expense.paymentMethod,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
        color = BentoTextSecondary
      )
    }
  }
}

fun getCategoryIcon(category: String, type: String): ImageVector {
  if (type == "INCOME") return Icons.Default.ArrowUpward
  return when (category.lowercase()) {
    "shopping" -> Icons.Default.ShoppingBag
    "housing", "rent" -> Icons.Default.Home
    "utilities" -> Icons.Default.ElectricBolt
    "food", "dining" -> Icons.Default.Receipt
    else -> Icons.Default.ArrowDownward
  }
}
