package com.mysticbyte.counter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DefaultSwatches = listOf(
    Color(0xFF1565C0), // Blue
    Color(0xFFC62828), // Red
    Color(0xFF2E7D32), // Green
    Color(0xFFFBC02D), // Yellow
    Color(0xFF6A1B9A), // Purple
    Color(0xFFEF6C00), // Orange
    Color(0xFF00838F), // Cyan
    Color(0xFF37474F), // Grey
    Color(0xFFAD1457), // Pink
    Color(0xFF00695C), // Teal
    Color(0xFF5E35B1), // Deep Purple
    Color(0xFFD84315), // Deep Orange
)

private fun Color.toHex(): String {
    val r = (this.red * 255).toInt().coerceIn(0, 255)
    val g = (this.green * 255).toInt().coerceIn(0, 255)
    val b = (this.blue * 255).toInt().coerceIn(0, 255)
    val rgb = (r shl 16) or (g shl 8) or b
    return "#${rgb.toString(16).uppercase().padStart(6, '0')}"
}

private fun parseColor(hex: String): Color? {
    return try {
        val clean = hex.trim().removePrefix("#")
        val rgb = clean.toLong(16).toInt()
        Color(0xFF shl 24 or rgb)
    } catch (_: Exception) {
        null
    }
}

@Composable
fun ColorPickerDialog(
    currentColorHex: String?,
    onDismiss: () -> Unit,
    onConfirm: (String?) -> Unit
) {
    var selectedHex by remember { mutableStateOf(currentColorHex) }
    var customHex by remember { mutableStateOf(currentColorHex ?: "#1565C0") }
    var isCustomMode by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pick Counter Color", style = MaterialTheme.typography.headlineSmall) },
        text = {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                if (!isCustomMode) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier.fillMaxWidth().height(180.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(DefaultSwatches) { color ->
                            val hex = color.toHex()
                            val isSelected = selectedHex == hex
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (isSelected) 3.dp else (if (hex == "#37474F") 1.dp else 0.dp),
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else androidx.compose.ui.graphics.Color.LightGray,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedHex = hex }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // "None" option
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { selectedHex = null },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedHex == null) {
                                Box(
                                    modifier = Modifier.size(16.dp).clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                            }
                        }
                        Text("Default Theme Color", style = MaterialTheme.typography.bodyLarge)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { isCustomMode = true }) {
                        Text("Enter Custom Hex")
                    }
                } else {
                    OutlinedTextField(
                        value = customHex,
                        onValueChange = { customHex = it },
                        label = { Text("Hex Color (#RRGGBB)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Preview box
                    val previewColor = parseColor(customHex) ?: MaterialTheme.colorScheme.surface
                    Box(
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                            .background(previewColor)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { isCustomMode = false }) {
                        Text("Back to Palette")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val result = if (isCustomMode) customHex else selectedHex
                    onConfirm(result)
                    onDismiss()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
