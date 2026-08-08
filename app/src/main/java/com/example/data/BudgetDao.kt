package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
  @Query("SELECT * FROM budgets")
  fun getAllBudgets(): Flow<List<BudgetEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBudget(budget: BudgetEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBudgets(budgets: List<BudgetEntity>)

  @Query("SELECT COUNT(*) FROM budgets")
  suspend fun getBudgetCount(): Int
}
