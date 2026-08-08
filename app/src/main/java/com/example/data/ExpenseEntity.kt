package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val title: String,
  val amount: Double,
  val type: String, // "EXPENSE" or "INCOME"
  val category: String,
  val dateTimestamp: Long,
  val note: String = "",
  val paymentMethod: String = "Credit Card"
)
