package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CategoryBudgetStatus
import com.example.ui.theme.BentoCardSecondary
import com.example.ui.theme.BentoDarkBlue
import com.example.ui.theme.BentoRedAlert
import com.example.ui.theme.BentoRedAlertContainer
import com.example.ui.theme.BentoTextSecondary

@Composable
fun CategoryProgressBar(
  status: CategoryBudgetStatus,
  onEditBudget: () -> Unit,
  modifier: Modifier = Modifier
) {
  val categoryColor = getCategoryColor(status.category)
  val isExceeded = status.isExceeded
  val progress = (status.spent / status.limit.coerceAtLeast(1.0)).coerceIn(0.0, 1.0).toFloat()

  Surface(
    onClick = onEditBudget,
    shape = RoundedCornerShape(16.dp),
    color = BentoCardSecondary,
    modifier = modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(categoryColor)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Text(
          text = status.category,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = BentoDarkBlue,
          modifier = Modifier.weight(1f)
        )

        if (isExceeded) {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = BentoRedAlertContainer,
            modifier = Modifier.padding(end = 8.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Alert",
                tint = BentoRedAlert,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Exceeded",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                fontWeight = FontWeight.Bold,
                color = BentoRedAlert
              )
            }
          }
        }

        Text(
          text = "$${String.format("%.0f", status.spent)} / $${String.format("%.0f", status.limit)}",
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.SemiBold,
          color = if (isExceeded) BentoRedAlert else BentoDarkBlue
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Custom rounded progress bar
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(10.dp)
          .clip(RoundedCornerShape(5.dp))
          .background(Color.LightGray.copy(alpha = 0.4f))
      ) {
        Box(
          modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(fraction = progress)
            .clip(RoundedCornerShape(5.dp))
            .background(if (isExceeded) BentoRedAlert else categoryColor)
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Row(modifier = Modifier.fillMaxWidth()) {
        val pctInt = (status.percentage * 100).toInt()
        Text(
          text = "$pctInt% of budget used",
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
          color = if (isExceeded) BentoRedAlert else BentoTextSecondary
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
          text = "Tap to edit limit",
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
          color = BentoTextSecondary
        )
      }
    }
  }
}
