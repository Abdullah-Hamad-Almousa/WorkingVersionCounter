package com.mysticbyte.counter.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay

@Composable
fun AnimatedCounter(
    value: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.displayLarge,
    color: Color = MaterialTheme.colorScheme.primary
) {
    var lastValue by remember { mutableStateOf(value) }
    var isIncreasing by remember { mutableStateOf(false) }
    var isDecreasing by remember { mutableStateOf(false) }
    var isResetting by remember { mutableStateOf(false) }

    LaunchedEffect(value) {
        val previous = lastValue
        lastValue = value
        if (value > previous) {
            isIncreasing = true
            delay(300)
            isIncreasing = false
        } else if (value < previous) {
            isDecreasing = true
            delay(300)
            isDecreasing = false
        } else if (value == 0 && previous != 0) {
            isResetting = true
            delay(400)
            isResetting = false
        }
    }

    val changeColor = when {
        isIncreasing -> Color(0xFF2E7D32) // green
        isDecreasing -> Color(0xFFC62828) // red
        isResetting -> Color(0xFFF57C00) // orange
        else -> color
    }

    val animatedScale by animateFloatAsState(
        targetValue = when {
            isIncreasing || isDecreasing || isResetting -> 1.15f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "counterScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(animatedScale),
        contentAlignment = Alignment.Center
    ) {
        Crossfade(
            targetState = value,
            label = "counterCrossfade"
        ) { targetValue ->
            Text(
                text = targetValue.toString(),
                style = style,
                color = changeColor
            )
        }
    }
}
