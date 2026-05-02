package com.mysticbyte.counter.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mysticbyte.counter.data.model.AppTheme
import com.mysticbyte.counter.ui.theme.BluePrimary
import com.mysticbyte.counter.ui.theme.CompanyBluePrimary
import com.mysticbyte.counter.ui.theme.GamePrimary

@Composable
fun ThemeSelector(
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    val themes = listOf(
        AppTheme.LIGHT to "Light",
        AppTheme.DARK to "Dark",
        AppTheme.COMPANY to "Company",
        AppTheme.GAME to "Game"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Theme",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            themes.forEach { (theme, label) ->
                val isSelected = selectedTheme == theme
                val containerColor = when (theme) {
                    AppTheme.LIGHT -> if (isSelected) BluePrimary else MaterialTheme.colorScheme.surfaceVariant
                    AppTheme.DARK -> if (isSelected) BluePrimary else MaterialTheme.colorScheme.surfaceVariant
                    AppTheme.COMPANY -> if (isSelected) CompanyBluePrimary else MaterialTheme.colorScheme.surfaceVariant
                    AppTheme.GAME -> if (isSelected) GamePrimary else MaterialTheme.colorScheme.surfaceVariant
                }
                val contentColor = when {
                    isSelected && theme == AppTheme.GAME -> Color.Black
                    isSelected -> Color.White
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }

                Button(
                    onClick = { onThemeSelected(theme) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = containerColor,
                        contentColor = contentColor
                    )
                ) {
                    Text(label, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
