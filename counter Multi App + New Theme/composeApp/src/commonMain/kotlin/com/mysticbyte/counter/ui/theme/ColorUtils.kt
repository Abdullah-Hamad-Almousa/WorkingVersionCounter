package com.mysticbyte.counter.ui.theme

import androidx.compose.ui.graphics.Color

fun parseColor(hex: String?): Color? {
    if (hex == null || !hex.startsWith("#") || (hex.length != 7)) return null
    return try {
        val argb = hex.removePrefix("#").toLong(16).toInt()
        Color(0xFF000000.toInt() or argb)
    } catch (_: Exception) {
        null
    }
}
