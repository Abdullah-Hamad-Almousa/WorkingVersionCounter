package com.mysticbyte.counter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mysticbyte.counter.ui.theme.parseColor

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
)

private fun Color.toHex(): String {
    val r = (this.red * 255).toInt().coerceIn(0, 255)
    val g = (this.green * 255).toInt().coerceIn(0, 255)
    val b = (this.blue * 255).toInt().coerceIn(0, 255)
    val rgb = (r shl 16) or (g shl 8) or b
    return "#${rgb.toString(16).uppercase().padStart(6, '0')}"
}

@Composable
fun CreateCounterDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, startValue: Int, step: Int, colorHex: String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var startValue by remember { mutableStateOf("0") }
    var step by remember { mutableStateOf("1") }
    var selectedColorHex by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Counter", style = MaterialTheme.typography.headlineSmall) },
        text = {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = startValue,
                        onValueChange = { startValue = it.filter { c -> c.isDigit() || c == '-' } },
                        label = { Text("Start Value") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = step,
                        onValueChange = { step = it.filter { c -> c.isDigit() } },
                        label = { Text("Step") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Color (optional)", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(
                                    width = if (selectedColorHex == null) 3.dp else 1.dp,
                                    color = if (selectedColorHex == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                    shape = CircleShape
                                )
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable { selectedColorHex = null },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Default color", tint = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                    items(DefaultSwatches) { color ->
                        val hex = color.toHex()
                        val isSelected = selectedColorHex == hex
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else androidx.compose.ui.graphics.Color.LightGray,
                                    shape = CircleShape
                                )
                                .clickable { selectedColorHex = hex }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val sv = startValue.toIntOrNull() ?: 0
                    val st = step.toIntOrNull() ?: 1
                    if (st > 0) {
                        onConfirm(name, sv, st, selectedColorHex)
                        onDismiss()
                    }
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
