package com.mysticbyte.counter.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mysticbyte.counter.ui.components.CounterCard
import com.mysticbyte.counter.ui.components.CreateCounterDialog
import com.mysticbyte.counter.ui.viewmodel.CountersViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToMenu: () -> Unit
) {
    val viewModel: CountersViewModel = koinViewModel()
    val counters by viewModel.counters.collectAsState()

    var showCreateDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Counters",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateToMenu) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Apps",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add counter")
            }
        }
    ) { innerPadding ->
        if (counters.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No counters yet.\nTap + to create one.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(counters, key = { it.id }) { counter ->
                    val onIncrement = remember(counter.id) { { viewModel.increment(counter.id) } }
                    val onDecrement = remember(counter.id) { { viewModel.decrement(counter.id) } }
                    val onDelete = remember(counter.id) { { viewModel.deleteCounter(counter.id) } }
                    val onClick = remember(counter.id) { { onNavigateToDetail(counter.id) } }
                    CounterCard(
                        counter = counter,
                        onIncrement = onIncrement,
                        onDecrement = onDecrement,
                        onDelete = onDelete,
                        onClick = onClick
                    )
                }
            }
        }
    }

        if (showCreateDialog) {
            CreateCounterDialog(
                onDismiss = { showCreateDialog = false },
                onConfirm = { name, startValue, step, colorHex ->
                    viewModel.addCounter(name, startValue, step, colorHex)
                }
            )
        }
}
