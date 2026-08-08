package com.example.data

import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class ExpenseRepository(private val database: AppDatabase) {
  private val expenseDao = database.expenseDao()
  private val budgetDao = database.budgetDao()

  val allExpenses: Flow<List<ExpenseEntity>> = expenseDao.getAllExpenses()
  val allBudgets: Flow<List<BudgetEntity>> = budgetDao.getAllBudgets()

  fun searchExpenses(query: String): Flow<List<ExpenseEntity>> {
    return if (query.isBlank()) {
      expenseDao.getAllExpenses()
    } else {
      expenseDao.searchExpenses(query)
    }
  }

  suspend fun insertExpense(expense: ExpenseEntity): Long {
    return expenseDao.insertExpense(expense)
  }

  suspend fun deleteExpense(expense: ExpenseEntity) {
    expenseDao.deleteExpense(expense)
  }

  suspend fun deleteExpenseById(id: Long) {
    expenseDao.deleteExpenseById(id)
  }

  suspend fun setBudget(category: String, limit: Double) {
    budgetDao.insertBudget(BudgetEntity(category, limit))
  }

  suspend fun seedInitialDataIfEmpty() {
    val expenseCount = expenseDao.getExpenseCount()
    if (expenseCount == 0) {
      val now = System.currentTimeMillis()
      val dayMs = 24 * 60 * 60 * 1000L

      val sampleExpenses = listOf(
        ExpenseEntity(
          title = "Rent Payment",
          amount = 1200.0,
          type = "EXPENSE",
          category = "Housing",
          dateTimestamp = now - (1 * dayMs),
          note = "Monthly apartment rent",
          paymentMethod = "Bank Transfer"
        ),
        ExpenseEntity(
          title = "Electricity Bill",
          amount = 142.50,
          type = "EXPENSE",
          category = "Utilities",
          dateTimestamp = now - (2 * dayMs),
          note = "Power & Gas utility",
          paymentMethod = "Credit Card"
        ),
        ExpenseEntity(
          title = "Salary Deposit",
          amount = 4200.0,
          type = "INCOME",
          category = "Salary",
          dateTimestamp = now - (4 * dayMs),
          note = "Bi-weekly paycheck",
          paymentMethod = "Direct Deposit"
        ),
        ExpenseEntity(
          title = "Shopping spree",
          amount = 840.0,
          type = "EXPENSE",
          category = "Shopping",
          dateTimestamp = now - (5 * dayMs),
          note = "Winter clothes and electronics",
          paymentMethod = "Credit Card"
        ),
        ExpenseEntity(
          title = "Whole Foods Market",
          amount = 185.20,
          type = "EXPENSE",
          category = "Food",
          dateTimestamp = now - (6 * dayMs),
          note = "Weekly organic groceries",
          paymentMethod = "Apple Pay / Debit"
        ),
        ExpenseEntity(
          title = "Uber Rides & Transit",
          amount = 64.00,
          type = "EXPENSE",
          category = "Transport",
          dateTimestamp = now - (8 * dayMs),
          note = "Commute to office",
          paymentMethod = "Credit Card"
        ),
        ExpenseEntity(
          title = "Freelance Design Client",
          amount = 850.0,
          type = "INCOME",
          category = "Salary",
          dateTimestamp = now - (10 * dayMs),
          note = "UI Kit consulting fee",
          paymentMethod = "Bank Transfer"
        ),
        ExpenseEntity(
          title = "Cinema & Dinner Night",
          amount = 120.00,
          type = "EXPENSE",
          category = "Entertainment",
          dateTimestamp = now - (12 * dayMs),
          note = "Movie tickets & restaurant",
          paymentMethod = "Credit Card"
        )
      )

      expenseDao.insertExpenses(sampleExpenses)
    }

    val budgetCount = budgetDao.getBudgetCount()
    if (budgetCount == 0) {
      val defaultBudgets = listOf(
        BudgetEntity(category = "Shopping", monthlyLimit = 600.0),
        BudgetEntity(category = "Housing", monthlyLimit = 1500.0),
        BudgetEntity(category = "Food", monthlyLimit = 500.0),
        BudgetEntity(category = "Utilities", monthlyLimit = 250.0),
        BudgetEntity(category = "Entertainment", monthlyLimit = 300.0),
        BudgetEntity(category = "Transport", monthlyLimit = 200.0),
        BudgetEntity(category = "Health", monthlyLimit = 200.0),
        BudgetEntity(category = "Other", monthlyLimit = 300.0)
      )
      budgetDao.insertBudgets(defaultBudgets)
    }
  }
}
