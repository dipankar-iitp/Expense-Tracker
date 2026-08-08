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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ExpenseEntity
import com.example.ui.ExpenseViewModel
import com.example.ui.theme.BentoBlueContainer
import com.example.ui.theme.BentoBluePrimary
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoCardSecondary
import com.example.ui.theme.BentoDarkBlue
import com.example.ui.theme.BentoGreenPrimary
import com.example.ui.theme.BentoRedAlert
import com.example.ui.theme.BentoRedAlertContainer
import com.example.ui.theme.BentoSurface
import com.example.ui.theme.BentoTextPrimary
import com.example.ui.theme.BentoTextSecondary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(
  viewModel: ExpenseViewModel,
  modifier: Modifier = Modifier
) {
  val expenses by viewModel.filteredExpenses.collectAsState()
  val query by viewModel.searchQuery.collectAsState()
  val selectedFilter by viewModel.selectedFilter.collectAsState()

  // Group transactions by Date String
  val groupedExpenses = remember(expenses) {
    expenses.groupBy { expense ->
      formatDateHeader(expense.dateTimestamp)
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Transaction History",
      style = MaterialTheme.typography.headlineMedium,
      fontWeight = FontWeight.Bold,
      color = BentoDarkBlue
    )

    Text(
      text = "Detailed, searchable log of all financial activity",
      style = MaterialTheme.typography.bodyMedium,
      color = BentoTextSecondary
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Search Bar
    OutlinedTextField(
      value = query,
      onValueChange = { viewModel.setSearchQuery(it) },
      placeholder = { Text("Search description, category, or note...") },
      leadingIcon = {
        Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = BentoTextSecondary)
      },
      trailingIcon = {
        if (query.isNotEmpty()) {
          IconButton(onClick = { viewModel.setSearchQuery("") }) {
            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = BentoTextSecondary)
          }
        }
      },
      singleLine = true,
      shape = RoundedCornerShape(16.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BentoBluePrimary,
        unfocusedBorderColor = BentoBorder,
        focusedContainerColor = BentoSurface,
        unfocusedContainerColor = BentoSurface
      ),
      modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Filter Chips
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      FilterChip(
        selected = selectedFilter == "ALL",
        onClick = { viewModel.setFilter("ALL") },
        label = { Text("All") },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = BentoBluePrimary,
          selectedLabelColor = BentoSurface,
          containerColor = BentoCardSecondary,
          labelColor = BentoDarkBlue
        ),
        shape = RoundedCornerShape(12.dp)
      )

      FilterChip(
        selected = selectedFilter == "EXPENSE",
        onClick = { viewModel.setFilter("EXPENSE") },
        label = { Text("Expenses") },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = BentoRedAlert,
          selectedLabelColor = BentoSurface,
          containerColor = BentoCardSecondary,
          labelColor = BentoDarkBlue
        ),
        shape = RoundedCornerShape(12.dp)
      )

      FilterChip(
        selected = selectedFilter == "INCOME",
        onClick = { viewModel.setFilter("INCOME") },
        label = { Text("Income") },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = BentoGreenPrimary,
          selectedLabelColor = BentoSurface,
          containerColor = BentoCardSecondary,
          labelColor = BentoDarkBlue
        ),
        shape = RoundedCornerShape(12.dp)
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    if (groupedExpenses.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.FilterList,
            contentDescription = "Empty",
            tint = BentoTextSecondary,
            modifier = Modifier.size(48.dp)
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = if (query.isBlank()) "No transactions found" else "No matching transactions for '$query'",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = BentoDarkBlue
          )
          Text(
            text = "Try clearing filters or adding a new transaction.",
            style = MaterialTheme.typography.bodySmall,
            color = BentoTextSecondary
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier.weight(1f),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
      ) {
        groupedExpenses.forEach { (dateHeader, itemsInGroup) ->
          item {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = BentoCardSecondary,
              modifier = Modifier.padding(vertical = 4.dp)
            ) {
              Text(
                text = dateHeader.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, letterSpacing = 1.sp),
                fontWeight = FontWeight.Bold,
                color = BentoTextSecondary,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
              )
            }
          }

          items(itemsInGroup, key = { it.id }) { expense ->
            val dismissState = rememberSwipeToDismissBoxState(
              confirmValueChange = { value ->
                if (value == SwipeToDismissBoxValue.EndToStart) {
                  viewModel.deleteExpense(expense)
                  true
                } else false
              }
            )

            SwipeToDismissBox(
              state = dismissState,
              backgroundContent = {
                Box(
                  modifier = Modifier
                    .fillMaxSize()
                    .background(BentoRedAlertContainer, RoundedCornerShape(16.dp))
                    .padding(horizontal = 20.dp),
                  contentAlignment = Alignment.CenterEnd
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = "Swipe to Delete",
                      style = MaterialTheme.typography.labelMedium,
                      color = BentoRedAlert,
                      fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                      imageVector = Icons.Default.Delete,
                      contentDescription = "Delete",
                      tint = BentoRedAlert
                    )
                  }
                }
              },
              enableDismissFromStartToEnd = false,
              enableDismissFromEndToStart = true
            ) {
              Surface(
                shape = RoundedCornerShape(16.dp),
                color = BentoSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorder)
              ) {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                  TransactionItemRow(
                    expense = expense,
                    onDeleteClick = { viewModel.deleteExpense(expense) }
                  )
                }
              }
            }
          }
        }

        item {
          Spacer(modifier = Modifier.height(80.dp))
        }
      }
    }
  }
}

fun formatDateHeader(timestamp: Long): String {
  val now = Calendar.getInstance()
  val itemCal = Calendar.getInstance().apply { timeInMillis = timestamp }

  val isToday = now.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR) &&
      now.get(Calendar.DAY_OF_YEAR) == itemCal.get(Calendar.DAY_OF_YEAR)

  val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
  val isYesterday = yesterday.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR) &&
      yesterday.get(Calendar.DAY_OF_YEAR) == itemCal.get(Calendar.DAY_OF_YEAR)

  return when {
    isToday -> "Today"
    isYesterday -> "Yesterday"
    else -> SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault()).format(Date(timestamp))
  }
}
