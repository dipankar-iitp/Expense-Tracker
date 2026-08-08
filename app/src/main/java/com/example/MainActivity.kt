package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ui.ExpenseViewModel
import com.example.ui.components.BentoNavigationBar
import com.example.ui.components.NavTab
import com.example.ui.screens.AddExpenseScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.SpendingInsightsScreen
import com.example.ui.screens.TransactionHistoryScreen
import com.example.ui.theme.BentoBackground
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  private val viewModel: ExpenseViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        ExpenseTrackerApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun ExpenseTrackerApp(viewModel: ExpenseViewModel) {
  var selectedTab by remember { mutableStateOf(NavTab.HOME) }
  var initialCategoryToEdit by remember { mutableStateOf<String?>(null) }

  Scaffold(
    bottomBar = {
      BentoNavigationBar(
        selectedTab = selectedTab,
        onTabSelected = { tab -> selectedTab = tab }
      )
    },
    containerColor = BentoBackground,
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (selectedTab) {
        NavTab.HOME -> {
          DashboardScreen(
            viewModel = viewModel,
            onSeeAllClick = { selectedTab = NavTab.ACTIVITY },
            onAdjustBudgetClick = { category ->
              initialCategoryToEdit = category
              selectedTab = NavTab.ANALYTICS
            }
          )
        }
        NavTab.ACTIVITY -> {
          TransactionHistoryScreen(viewModel = viewModel)
        }
        NavTab.ADD -> {
          AddExpenseScreen(
            viewModel = viewModel,
            onTransactionSaved = { selectedTab = NavTab.HOME }
          )
        }
        NavTab.ANALYTICS, NavTab.SETTINGS -> {
          SpendingInsightsScreen(
            viewModel = viewModel,
            initialCategoryToEdit = initialCategoryToEdit
          )
        }
      }
    }
  }
}

