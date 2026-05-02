package com.mysticbyte.counter.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mysticbyte.counter.data.model.AppStatus
import com.mysticbyte.counter.data.model.MicroApp
import com.mysticbyte.counter.ui.components.AppCard

val APPS = listOf(
    MicroApp(
        id = "counter",
        name = "Counter",
        status = AppStatus.AVAILABLE,
        icon = Icons.AutoMirrored.Filled.FormatListBulleted,
        description = "Track and count anything"
    ),
    MicroApp(
        id = "coming_soon",
        name = "Coming Soon",
        status = AppStatus.COMING_SOON,
        icon = Icons.Default.HourglassEmpty,
        description = "New app coming soon"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LauncherScreen(
    onLaunchApp: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Apps",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select an application",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(APPS, key = { it.id }) { app ->
                    AppCard(
                        app = app,
                        onClick = {
                            if (app.status == AppStatus.AVAILABLE) {
                                onLaunchApp(app.id)
                            } else {
                                onLaunchApp("coming_soon")
                            }
                        }
                    )
                }
            }
        }
    }
}
