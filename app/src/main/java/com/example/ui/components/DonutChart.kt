package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CategoryBudgetStatus
import com.example.ui.theme.BentoDarkBlue
import com.example.ui.theme.BentoTextSecondary
import com.example.ui.theme.CategoryEntertainment
import com.example.ui.theme.CategoryFood
import com.example.ui.theme.CategoryHealth
import com.example.ui.theme.CategoryHousing
import com.example.ui.theme.CategoryOther
import com.example.ui.theme.CategorySalary
import com.example.ui.theme.CategoryShopping
import com.example.ui.theme.CategoryTransport
import com.example.ui.theme.CategoryUtilities
import kotlin.math.atan2

fun getCategoryColor(category: String): Color {
  return when (category.lowercase()) {
    "shopping" -> CategoryShopping
    "housing", "rent" -> CategoryHousing
    "food", "dining" -> CategoryFood
    "utilities" -> CategoryUtilities
    "entertainment" -> CategoryEntertainment
    "transport" -> CategoryTransport
    "health" -> CategoryHealth
    "salary" -> CategorySalary
    else -> CategoryOther
  }
}

@Composable
fun DonutChart(
  statuses: List<CategoryBudgetStatus>,
  modifier: Modifier = Modifier
) {
  val totalSpent = remember(statuses) { statuses.sumOf { it.spent } }
  var selectedCategory by remember { mutableStateOf<CategoryBudgetStatus?>(null) }

  if (statuses.isEmpty() || totalSpent == 0.0) {
    Box(
      modifier = modifier.height(180.dp),
      contentAlignment = Alignment.Center
    ) {
      Text("No expense data for analytics", color = BentoTextSecondary)
    }
    return
  }

  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      modifier = Modifier
        .size(200.dp)
        .padding(8.dp),
      contentAlignment = Alignment.Center
    ) {
      Canvas(
        modifier = Modifier
          .size(190.dp)
          .pointerInput(statuses) {
            detectTapGestures { tapOffset ->
              val center = Offset(size.width / 2f, size.height / 2f)
              val dx = tapOffset.x - center.x
              val dy = tapOffset.y - center.y
              var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
              if (angle < 0) angle += 360f

              // Adjust for starting angle -90 degrees
              var adjustedAngle = (angle + 90f) % 360f

              var currentStart = 0f
              for (item in statuses) {
                val sweep = ((item.spent / totalSpent) * 360f).toFloat()
                if (adjustedAngle in currentStart..(currentStart + sweep)) {
                  selectedCategory = item
                  break
                }
                currentStart += sweep
              }
            }
          }
      ) {
        val strokeWidth = 28.dp.toPx()
        val diameter = size.minDimension - strokeWidth
        val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
        val arcSize = Size(diameter, diameter)

        var startAngle = -90f

        statuses.forEach { item ->
          val sweepAngle = ((item.spent / totalSpent) * 360f).toFloat()
          val isSelected = selectedCategory?.category == item.category
          val drawStroke = if (isSelected) strokeWidth + 8.dp.toPx() else strokeWidth

          drawArc(
            color = getCategoryColor(item.category),
            startAngle = startAngle,
            sweepAngle = sweepAngle - 2f, // gap
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = drawStroke)
          )

          startAngle += sweepAngle
        }
      }

      // Inner Donut Info Text
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        selectedCategory?.let { sel ->
          Text(
            text = sel.category,
            style = MaterialTheme.typography.labelMedium,
            color = BentoTextSecondary
          )
          Text(
            text = "$${String.format("%.2f", sel.spent)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = BentoDarkBlue
          )
          Text(
            text = "${String.format("%.1f", (sel.spent / totalSpent) * 100)}%",
            style = MaterialTheme.typography.bodySmall,
            color = getCategoryColor(sel.category),
            fontWeight = FontWeight.Bold
          )
        } ?: run {
          Text(
            text = "Total Spent",
            style = MaterialTheme.typography.labelMedium,
            color = BentoTextSecondary
          )
          Text(
            text = "$${String.format("%.2f", totalSpent)}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = BentoDarkBlue
          )
          Text(
            text = "Tap segment",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
            color = BentoTextSecondary
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Legend Grid
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
      statuses.chunked(2).forEach { rowItems ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
        ) {
          rowItems.forEach { item ->
            val color = getCategoryColor(item.category)
            val pct = ((item.spent / totalSpent) * 100).toInt()
            Surface(
              onClick = { selectedCategory = item },
              shape = MaterialTheme.shapes.small,
              color = if (selectedCategory?.category == item.category) color.copy(alpha = 0.15f) else Color.Transparent,
              modifier = Modifier.weight(1f)
            ) {
              Row(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Canvas(modifier = Modifier.size(10.dp)) {
                  drawCircle(color = color)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = item.category,
                  style = MaterialTheme.typography.bodySmall,
                  fontWeight = FontWeight.Medium,
                  color = BentoDarkBlue,
                  modifier = Modifier.weight(1f)
                )
                Text(
                  text = "$pct%",
                  style = MaterialTheme.typography.bodySmall,
                  fontWeight = FontWeight.Bold,
                  color = BentoTextSecondary
                )
              }
            }
          }
          if (rowItems.size == 1) {
            Spacer(modifier = Modifier.weight(1f))
          }
        }
      }
    }
  }
}
