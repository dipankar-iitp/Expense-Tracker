package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.ui.theme.BentoBlueCardCircle
import com.example.ui.theme.BentoBlueContainer
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoCardSecondary
import com.example.ui.theme.BentoDarkBlue
import com.example.ui.theme.BentoDarkGreenCard
import com.example.ui.theme.BentoGreenContainer
import com.example.ui.theme.BentoGreenPrimary
import com.example.ui.theme.BentoOnBlueContainer
import com.example.ui.theme.BentoOnGreenContainer
import com.example.ui.theme.BentoRedAlert
import com.example.ui.theme.BentoRedAlertContainer
import com.example.ui.theme.BentoSurface
import com.example.ui.theme.BentoTextPrimary
import com.example.ui.theme.BentoTextSecondary

@Composable
fun BentoHeader(
  userName: String = "Alex Rivera",
  onProfileClick: () -> Unit = {}
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column {
      Text(
        text = "WELCOME BACK",
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, letterSpacing = 1.2.sp),
        color = BentoTextSecondary,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = userName,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = BentoDarkBlue
      )
    }
    Spacer(modifier = Modifier.weight(1f))
    Surface(
      onClick = onProfileClick,
      shape = CircleShape,
      color = BentoBlueContainer,
      border = androidx.compose.foundation.BorderStroke(2.dp, BentoSurface),
      shadowElevation = 2.dp,
      modifier = Modifier.size(48.dp)
    ) {
      Box(contentAlignment = Alignment.Center) {
        Icon(
          imageVector = Icons.Default.Person,
          contentDescription = "Profile",
          tint = BentoOnBlueContainer,
          modifier = Modifier.size(28.dp)
        )
      }
    }
  }
}

@Composable
fun HeroBalanceBentoCard(
  totalBalance: Double,
  monthlyTrendPct: String = "+12% this month",
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(32.dp),
    color = BentoBlueContainer,
    modifier = modifier
      .fillMaxWidth()
      .height(160.dp)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(24.dp)
    ) {
      // Background Decorative Circle
      Box(
        modifier = Modifier
          .size(128.dp)
          .align(Alignment.BottomEnd)
          .padding(top = 16.dp, start = 16.dp)
          .clip(CircleShape)
          .background(BentoBlueCardCircle.copy(alpha = 0.4f))
      )

      Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = "Total Balance",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            color = BentoOnBlueContainer
          )
          Text(
            text = "$${String.format("%,.2f", totalBalance)}",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = BentoDarkBlue
          )
        }

        Surface(
          shape = CircleShape,
          color = BentoGreenContainer,
          modifier = Modifier.padding(top = 8.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.TrendingUp,
              contentDescription = "Trend",
              tint = BentoGreenPrimary,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = monthlyTrendPct,
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = BentoOnGreenContainer
            )
          }
        }
      }
    }
  }
}

@Composable
fun BentoGridRow(
  topCategory: CategoryBudgetStatus?,
  totalSavings: Double,
  onShoppingClick: () -> Unit,
  onSavingsClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
  ) {
    // Left Bento Card (Shopping / Alert)
    Surface(
      onClick = onShoppingClick,
      shape = RoundedCornerShape(24.dp),
      color = BentoSurface,
      border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorder),
      modifier = Modifier
        .weight(1f)
        .height(128.dp)
    ) {
      Column(
        modifier = Modifier
          .padding(16.dp)
          .fillMaxHeight(),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(BentoCardSecondary),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.ShoppingBag,
              contentDescription = "Shopping",
              tint = BentoTextSecondary,
              modifier = Modifier.size(20.dp)
            )
          }

          if (topCategory?.isExceeded == true) {
            Surface(
              shape = CircleShape,
              color = BentoRedAlertContainer
            ) {
              Text(
                text = "Alert",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                fontWeight = FontWeight.Bold,
                color = BentoRedAlert,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
              )
            }
          }
        }

        Column {
          Text(
            text = topCategory?.category ?: "Shopping",
            style = MaterialTheme.typography.bodySmall,
            color = BentoTextSecondary
          )
          Text(
            text = "$${String.format("%,.2f", topCategory?.spent ?: 840.0)}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = BentoTextPrimary
          )
        }
      }
    }

    // Right Bento Card (Savings / Income - Green Theme)
    Surface(
      onClick = onSavingsClick,
      shape = RoundedCornerShape(24.dp),
      color = BentoGreenPrimary,
      modifier = Modifier
        .weight(1f)
        .height(128.dp)
    ) {
      Column(
        modifier = Modifier
          .padding(16.dp)
          .fillMaxHeight(),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
      ) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(BentoDarkGreenCard),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.AccountBalanceWallet,
            contentDescription = "Savings",
            tint = BentoSurface,
            modifier = Modifier.size(20.dp)
          )
        }

        Column {
          Text(
            text = "Total Income",
            style = MaterialTheme.typography.bodySmall,
            color = BentoSurface.copy(alpha = 0.8f)
          )
          Text(
            text = "$${String.format("%,.2f", totalSavings)}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = BentoSurface
          )
        }
      }
    }
  }
}

@Composable
fun SmartBudgetAlertCard(
  alerts: List<CategoryBudgetStatus>,
  onAdjustBudgetClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  if (alerts.isEmpty()) return

  val firstAlert = alerts.first()
  val excess = firstAlert.spent - firstAlert.limit

  Surface(
    shape = RoundedCornerShape(20.dp),
    color = BentoRedAlertContainer,
    border = androidx.compose.foundation.BorderStroke(1.dp, BentoRedAlert.copy(alpha = 0.3f)),
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(BentoRedAlert),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Warning,
          contentDescription = "Alert",
          tint = BentoSurface,
          modifier = Modifier.size(22.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "Budget Limit Exceeded!",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = BentoRedAlert
        )
        Text(
          text = "${firstAlert.category} is $${String.format("%.2f", excess)} over monthly limit ($${String.format("%.0f", firstAlert.limit)})",
          style = MaterialTheme.typography.bodySmall,
          color = BentoDarkBlue
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      Button(
        onClick = { onAdjustBudgetClick(firstAlert.category) },
        colors = ButtonDefaults.buttonColors(containerColor = BentoRedAlert),
        shape = RoundedCornerShape(12.dp)
      ) {
        Text(
          text = "Adjust",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = BentoSurface
        )
      }
    }
  }
}
