package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.TrendPoint
import com.example.ui.theme.BentoBluePrimary
import com.example.ui.theme.BentoDarkBlue
import com.example.ui.theme.BentoTextSecondary

@Composable
fun SpendingLineChart(
  points: List<TrendPoint>,
  modifier: Modifier = Modifier
) {
  if (points.isEmpty()) return

  var selectedPointIndex by remember { mutableStateOf<Int?>(null) }
  val maxAmount = remember(points) { points.maxOfOrNull { it.amount }?.coerceAtLeast(100f) ?: 100f }

  Column(modifier = modifier) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Weekly Spending Trend",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = BentoDarkBlue
      )
      Spacer(modifier = Modifier.weight(1f))
      selectedPointIndex?.let { index ->
        val p = points[index]
        Text(
          text = "${p.label}: $${String.format("%.2f", p.amount)}",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold,
          color = BentoBluePrimary
        )
      } ?: Text(
        text = "Tap node to inspect",
        style = MaterialTheme.typography.bodySmall,
        color = BentoTextSecondary
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(140.dp)
    ) {
      Canvas(
        modifier = Modifier
          .fillMaxSize()
          .pointerInput(points) {
            detectTapGestures { tapOffset ->
              val width = size.width
              val stepX = width / (points.size - 1).coerceAtLeast(1)
              val index = (tapOffset.x / stepX).toInt().coerceIn(0, points.size - 1)
              selectedPointIndex = index
            }
          }
      ) {
        val width = size.width
        val height = size.height
        val paddingBottom = 24.dp.toPx()
        val usableHeight = height - paddingBottom
        val stepX = width / (points.size - 1).coerceAtLeast(1)

        // Draw horizontal grid lines
        val gridLines = 3
        for (i in 0..gridLines) {
          val y = usableHeight * (i.toFloat() / gridLines)
          drawLine(
            color = Color.LightGray.copy(alpha = 0.4f),
            start = Offset(0f, y),
            end = Offset(width, y),
            strokeWidth = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
          )
        }

        // Calculate smooth curve control points
        val pointCoordinates = points.mapIndexed { i, p ->
          val x = i * stepX
          val y = usableHeight - ((p.amount / maxAmount) * usableHeight * 0.85f)
          Offset(x, y)
        }

        // Build path for smooth curve
        val path = Path().apply {
          if (pointCoordinates.isNotEmpty()) {
            moveTo(pointCoordinates[0].x, pointCoordinates[0].y)
            for (i in 0 until pointCoordinates.size - 1) {
              val p1 = pointCoordinates[i]
              val p2 = pointCoordinates[i + 1]
              val controlX1 = p1.x + (p2.x - p1.x) / 2f
              val controlY1 = p1.y
              val controlX2 = p1.x + (p2.x - p1.x) / 2f
              val controlY2 = p2.y
              cubicTo(controlX1, controlY1, controlX2, controlY2, p2.x, p2.y)
            }
          }
        }

        // Fill background gradient below path
        val fillPath = Path().apply {
          addPath(path)
          lineTo(width, height)
          lineTo(0f, height)
          close()
        }

        drawPath(
          path = fillPath,
          brush = Brush.verticalGradient(
            colors = listOf(
              BentoBluePrimary.copy(alpha = 0.3f),
              BentoBluePrimary.copy(alpha = 0.0f)
            )
          )
        )

        // Draw main line
        drawPath(
          path = path,
          color = BentoBluePrimary,
          style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw point dots
        pointCoordinates.forEachIndexed { index, point ->
          val isSelected = selectedPointIndex == index
          val radius = if (isSelected) 7.dp.toPx() else 4.dp.toPx()

          drawCircle(
            color = Color.White,
            radius = radius + 2.dp.toPx(),
            center = point
          )
          drawCircle(
            color = if (isSelected) BentoDarkBlue else BentoBluePrimary,
            radius = radius,
            center = point
          )
        }
      }
    }

    // X-Axis Labels
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 4.dp),
      horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
    ) {
      points.forEach { p ->
        Text(
          text = p.label,
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
          color = BentoTextSecondary,
          fontWeight = FontWeight.Medium
        )
      }
    }
  }
}
