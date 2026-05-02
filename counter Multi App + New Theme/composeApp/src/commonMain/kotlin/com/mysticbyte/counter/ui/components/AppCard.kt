package com.mysticbyte.counter.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mysticbyte.counter.data.model.AppStatus
import com.mysticbyte.counter.data.model.MicroApp

@Composable
fun AppCard(
    app: MicroApp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAvailable = app.status == AppStatus.AVAILABLE

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = if (isAvailable) 0.85f else 0.5f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = if (isAvailable) 0.3f else 0.15f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isAvailable) 6.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = app.icon,
                contentDescription = app.name,
                modifier = Modifier.size(48.dp),
                tint = if (isAvailable)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = app.name,
                style = MaterialTheme.typography.titleMedium,
                color = if (isAvailable)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!isAvailable) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Coming Soon",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
