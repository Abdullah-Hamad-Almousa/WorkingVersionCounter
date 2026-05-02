package com.mysticbyte.counter.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mysticbyte.counter.data.model.ActionType
import com.mysticbyte.counter.data.model.CounterEvent

private fun ActionType.toIcon(): ImageVector = when (this) {
    ActionType.INCREMENT -> Icons.AutoMirrored.Filled.TrendingUp
    ActionType.DECREMENT -> Icons.AutoMirrored.Filled.TrendingDown
    ActionType.RESET -> Icons.Default.Replay
    ActionType.ADD -> Icons.Default.Add
    ActionType.SET -> Icons.Default.Create
    ActionType.STEP_CHANGE -> Icons.Default.AddCircle
    ActionType.NAME_CHANGE -> Icons.Default.TextFields
    ActionType.COLOR_CHANGE -> Icons.Default.Brush
    ActionType.CREATED -> Icons.AutoMirrored.Filled.ArrowForward
}

private fun ActionType.toDisplayName(): String = when (this) {
    ActionType.INCREMENT -> "Incremented"
    ActionType.DECREMENT -> "Decremented"
    ActionType.RESET -> "Reset"
    ActionType.ADD -> "Added Number"
    ActionType.SET -> "Set Value"
    ActionType.STEP_CHANGE -> "Step Changed"
    ActionType.NAME_CHANGE -> "Renamed"
    ActionType.COLOR_CHANGE -> "Color Updated"
    ActionType.CREATED -> "Created"
}

private fun ActionType.toTint(): Color = when (this) {
    ActionType.INCREMENT -> Color(0xFF2E7D32)
    ActionType.DECREMENT -> Color(0xFFC62828)
    ActionType.RESET -> Color(0xFFF57C00)
    ActionType.ADD -> Color(0xFF1565C0)
    ActionType.SET -> Color(0xFF6A1B9A)
    ActionType.STEP_CHANGE -> Color(0xFF00838F)
    ActionType.NAME_CHANGE -> Color(0xFF37474F)
    ActionType.COLOR_CHANGE -> Color(0xFFAD1457)
    ActionType.CREATED -> Color(0xFF2E7D32)
}

@Composable
fun HistoryItem(event: CounterEvent, modifier: Modifier = Modifier) {
    val tint = event.action.toTint()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = event.action.toIcon(),
                contentDescription = event.action.toDisplayName(),
                tint = tint,
                modifier = Modifier
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.action.toDisplayName(),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (event.metadata != null && event.action != ActionType.CREATED) {
                    Text(
                        text = event.metadata,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (event.action == ActionType.CREATED) {
                    Text(
                        text = "Starting value: ${event.newValue}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${event.previousValue} → ${event.newValue}",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                if (event.amount != null && event.action != ActionType.CREATED) {
                    val sign = when {
                        event.newValue > event.previousValue -> "+"
                        event.newValue < event.previousValue -> ""
                        else -> ""
                    }
                    Text(
                        text = "($sign${event.amount})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
