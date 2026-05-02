package com.mysticbyte.counter.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddNumberDialog(
    currentValue: Int,
    onDismiss: () -> Unit,
    onAdd: (Int) -> Unit,
    onSet: (Int) -> Unit
) {
    var input by remember { mutableStateOf("") }
    var isSetMode by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add / Set Number", style = MaterialTheme.typography.headlineSmall) },
        text = {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it.filter { c -> c.isDigit() || c == '-' } },
                    label = { Text(if (isSetMode) "Set to Value" else "Amount to Add") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { isSetMode = false },
                        enabled = !isSetMode
                    ) {
                        Text("Add Mode")
                    }
                    Button(
                        onClick = { isSetMode = true },
                        enabled = !isSetMode
                    ) {
                        Text("Set Mode")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val value = input.toIntOrNull() ?: 0
                    if (isSetMode) {
                        onSet(value)
                    } else {
                        onAdd(value)
                    }
                    onDismiss()
                }
            ) {
                Text(if (isSetMode) "Set" else "Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
