package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BentoBlueContainer
import com.example.ui.theme.BentoBluePrimary
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoDarkBlue
import com.example.ui.theme.BentoNavBackground
import com.example.ui.theme.BentoSurface
import com.example.ui.theme.BentoTextSecondary

enum class NavTab {
  HOME, ACTIVITY, ADD, ANALYTICS, SETTINGS
}

@Composable
fun BentoNavigationBar(
  selectedTab: NavTab,
  onTabSelected: (NavTab) -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    color = BentoNavBackground,
    border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorder.copy(alpha = 0.5f)),
    modifier = modifier
      .fillMaxWidth()
      .height(84.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      NavItem(
        icon = Icons.Default.Home,
        label = "Home",
        isSelected = selectedTab == NavTab.HOME,
        onClick = { onTabSelected(NavTab.HOME) }
      )

      NavItem(
        icon = Icons.Default.ReceiptLong,
        label = "Activity",
        isSelected = selectedTab == NavTab.ACTIVITY,
        onClick = { onTabSelected(NavTab.ACTIVITY) }
      )

      // Center Floating Add Button
      Box(
        modifier = Modifier.offset(y = (-16).dp),
        contentAlignment = Alignment.Center
      ) {
        Surface(
          onClick = { onTabSelected(NavTab.ADD) },
          shape = RoundedCornerShape(20.dp),
          color = BentoBluePrimary,
          shadowElevation = 8.dp,
          border = androidx.compose.foundation.BorderStroke(3.dp, BentoSurface),
          modifier = Modifier.size(56.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "Add Expense",
              tint = BentoSurface,
              modifier = Modifier.size(32.dp)
            )
          }
        }
      }

      NavItem(
        icon = Icons.Default.Analytics,
        label = "Analytics",
        isSelected = selectedTab == NavTab.ANALYTICS,
        onClick = { onTabSelected(NavTab.ANALYTICS) }
      )

      NavItem(
        icon = Icons.Default.Settings,
        label = "Budgets",
        isSelected = selectedTab == NavTab.SETTINGS,
        onClick = { onTabSelected(NavTab.SETTINGS) }
      )
    }
  }
}

@Composable
private fun NavItem(
  icon: ImageVector,
  label: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    modifier = Modifier
      .clip(RoundedCornerShape(16.dp))
      .clickable { onClick() }
      .padding(horizontal = 12.dp, vertical = 4.dp)
  ) {
    if (isSelected) {
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(16.dp))
          .background(BentoBlueContainer)
          .padding(horizontal = 16.dp, vertical = 4.dp)
      ) {
        Icon(
          imageVector = icon,
          contentDescription = label,
          tint = BentoDarkBlue,
          modifier = Modifier.size(22.dp)
        )
      }
    } else {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = BentoTextSecondary,
        modifier = Modifier.size(22.dp)
      )
    }

    Spacer(modifier = Modifier.height(2.dp))

    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      color = if (isSelected) BentoDarkBlue else BentoTextSecondary
    )
  }
}
