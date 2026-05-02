package com.mysticbyte.counter.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mysticbyte.counter.ui.components.AddNumberDialog
import com.mysticbyte.counter.ui.components.AnimatedCounter
import com.mysticbyte.counter.ui.components.ColorPickerDialog
import com.mysticbyte.counter.ui.theme.parseColor
import com.mysticbyte.counter.ui.viewmodel.CountersViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterDetailScreen(
    counterId: String,
    onNavigateBack: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val viewModel: CountersViewModel = koinViewModel()
    val counters by viewModel.counters.collectAsState()
    val counter = counters.find { it.id == counterId }

    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var showColorPicker by rememberSaveable { mutableStateOf(false) }
    var editName by remember { mutableStateOf("") }
    var editStep by remember { mutableStateOf("") }
    var hasSeenCounter by remember { mutableStateOf(false) }

    LaunchedEffect(counter) {
        if (counter != null) {
            hasSeenCounter = true
            editName = counter.name
            editStep = counter.step.toString()
        }
    }

    if (counter == null) {
        LaunchedEffect(hasSeenCounter) {
            if (hasSeenCounter) onNavigateBack()
        }
        return
    }

    val safeCounter = counter
    val accentColor = remember(safeCounter.colorHex) { parseColor(safeCounter.colorHex) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(safeCounter.name, color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToHistory) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedCounter(
                value = safeCounter.value,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = accentColor ?: MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Step: ${safeCounter.step}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { viewModel.decrement(counterId) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text("-", style = MaterialTheme.typography.headlineMedium)
                }
                Button(
                    onClick = { viewModel.reset(counterId) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text("Reset", style = MaterialTheme.typography.labelLarge)
                }
                Button(
                    onClick = { viewModel.increment(counterId) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("+", style = MaterialTheme.typography.headlineMedium)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) {
                Text("Add / Set Number", style = MaterialTheme.typography.labelLarge)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Edit Counter",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = editName,
                onValueChange = { editName = it },
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = editStep,
                    onValueChange = { editStep = it.filter { c -> c.isDigit() } },
                    label = { Text("Step") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        viewModel.updateName(counterId, editName)
                        val newStep = editStep.toIntOrNull() ?: safeCounter.step
                        viewModel.updateStep(counterId, newStep)
                    },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text("Save")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { showColorPicker = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor?.copy(alpha = 0.2f)
                        ?: MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = accentColor ?: MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Change Color", style = MaterialTheme.typography.labelLarge)
            }
        }
    }

    if (showAddDialog) {
        AddNumberDialog(
            currentValue = safeCounter.value,
            onDismiss = { showAddDialog = false },
            onAdd = { amount -> viewModel.addToValue(counterId, amount) },
            onSet = { newValue -> viewModel.setValue(counterId, newValue) }
        )
    }

    if (showColorPicker) {
        ColorPickerDialog(
            currentColorHex = safeCounter.colorHex,
            onDismiss = { showColorPicker = false },
            onConfirm = { hex -> viewModel.updateColor(counterId, hex) }
        )
    }
}
