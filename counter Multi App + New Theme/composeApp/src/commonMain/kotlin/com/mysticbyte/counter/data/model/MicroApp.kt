package com.mysticbyte.counter.data.model

import androidx.compose.ui.graphics.vector.ImageVector

enum class AppStatus {
    AVAILABLE,
    COMING_SOON
}

data class MicroApp(
    val id: String,
    val name: String,
    val status: AppStatus,
    val icon: ImageVector,
    val description: String? = null
)
