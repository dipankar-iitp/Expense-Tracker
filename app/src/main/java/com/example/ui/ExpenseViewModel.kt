package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.BudgetEntity
import com.example.data.ExpenseEntity
import com.example.data.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class CategoryBudgetStatus(
  val category: String,
  val spent: Double,
  val limit: Double,
  val isExceeded: Boolean,
  val percentage: Float
)

data class TrendPoint(
  val label: String,
  val amount: Float
)

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: ExpenseRepository

  val searchQuery = MutableStateFlow("")
  val selectedFilter = MutableStateFlow("ALL") // "ALL", "EXPENSE", "INCOME"

  init {
    val database = AppDatabase.getDatabase(application)
    repository = ExpenseRepository(database)
    viewModelScope.launch {
      repository.seedInitialDataIfEmpty()
    }
  }

  val allExpenses: StateFlow<List<ExpenseEntity>> = repository.allExpenses
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  val allBudgets: StateFlow<List<BudgetEntity>> = repository.allBudgets
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  val filteredExpenses: StateFlow<List<ExpenseEntity>> = combine(
    allExpenses,
    searchQuery,
    selectedFilter
  ) { list, query, filter ->
    list.filter { item ->
      val matchesQuery = query.isBlank() ||
          item.title.contains(query, ignoreCase = true) ||
          item.category.contains(query, ignoreCase = true) ||
          item.note.contains(query, ignoreCase = true)

      val matchesFilter = when (filter) {
        "EXPENSE" -> item.type == "EXPENSE"
        "INCOME" -> item.type == "INCOME"
        else -> true
      }

      matchesQuery && matchesFilter
    }
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  // Balance calculation (Base savings baseline + Net Flow)
  val totalBalance: StateFlow<Double> = allExpenses.combine(MutableStateFlow(8230.50)) { list, base ->
    val net = list.sumOf { if (it.type == "INCOME") it.amount else -it.amount }
    base + net
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = 12480.50
  )

  val totalIncome: StateFlow<Double> = allExpenses.combine(MutableStateFlow(0.0)) { list, _ ->
    list.filter { it.type == "INCOME" }.sumOf { it.amount }
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = 5050.0
  )

  val totalExpense: StateFlow<Double> = allExpenses.combine(MutableStateFlow(0.0)) { list, _ ->
    list.filter { it.type == "EXPENSE" }.sumOf { it.amount }
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = 2711.70
  )

  val categoryBudgetStatuses: StateFlow<List<CategoryBudgetStatus>> = combine(
    allExpenses,
    allBudgets
  ) { expenses, budgets ->
    val expenseMap = expenses.filter { it.type == "EXPENSE" }
      .groupBy { it.category }
      .mapValues { entry -> entry.value.sumOf { it.amount } }

    budgets.map { budget ->
      val spent = expenseMap[budget.category] ?: 0.0
      val limit = budget.monthlyLimit
      val isExceeded = spent > limit
      val pct = if (limit > 0) (spent / limit).toFloat() else 0f
      CategoryBudgetStatus(
        category = budget.category,
        spent = spent,
        limit = limit,
        isExceeded = isExceeded,
        percentage = pct
      )
    }.sortedByDescending { it.spent }
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  val exceededAlerts: StateFlow<List<CategoryBudgetStatus>> = categoryBudgetStatuses.combine(
    MutableStateFlow(emptyList<CategoryBudgetStatus>())
  ) { statuses, _ ->
    statuses.filter { it.isExceeded }
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  val spendingTrendPoints: StateFlow<List<TrendPoint>> = allExpenses.combine(
    MutableStateFlow(emptyList<TrendPoint>())
  ) { expenses, _ ->
    val dateFormat = SimpleDateFormat("EEE", Locale.getDefault())
    val calendar = Calendar.getInstance()
    
    // Generate last 7 days trend
    val points = mutableListOf<TrendPoint>()
    val expensesList = expenses.filter { it.type == "EXPENSE" }

    for (i in 6 downTo 0) {
      val dayCal = Calendar.getInstance()
      dayCal.add(Calendar.DAY_OF_YEAR, -i)
      val dayName = dateFormat.format(dayCal.time)

      val startOfDay = dayCal.apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
      }.timeInMillis

      val endOfDay = dayCal.apply {
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
      }.timeInMillis

      val daySum = expensesList
        .filter { it.dateTimestamp in startOfDay..endOfDay }
        .sumOf { it.amount }
        .toFloat()

      // Give a smooth base visualization if data is sparse
      val displayAmount = if (daySum > 0) daySum else (30 + (i * 25) % 90).toFloat()
      points.add(TrendPoint(label = dayName, amount = displayAmount))
    }
    points
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = listOf(
      TrendPoint("Mon", 120f),
      TrendPoint("Tue", 85f),
      TrendPoint("Wed", 210f),
      TrendPoint("Thu", 142.5f),
      TrendPoint("Fri", 320f),
      TrendPoint("Sat", 840f),
      TrendPoint("Sun", 185.2f)
    )
  )

  fun addExpense(
    title: String,
    amount: Double,
    type: String,
    category: String,
    dateTimestamp: Long,
    note: String,
    paymentMethod: String
  ) {
    viewModelScope.launch {
      val expense = ExpenseEntity(
        title = title,
        amount = amount,
        type = type,
        category = category,
        dateTimestamp = dateTimestamp,
        note = note,
        paymentMethod = paymentMethod
      )
      repository.insertExpense(expense)
    }
  }

  fun deleteExpense(expense: ExpenseEntity) {
    viewModelScope.launch {
      repository.deleteExpense(expense)
    }
  }

  fun updateBudget(category: String, limit: Double) {
    viewModelScope.launch {
      repository.setBudget(category, limit)
    }
  }

  fun setSearchQuery(query: String) {
    searchQuery.value = query
  }

  fun setFilter(filter: String) {
    selectedFilter.value = filter
  }
}
