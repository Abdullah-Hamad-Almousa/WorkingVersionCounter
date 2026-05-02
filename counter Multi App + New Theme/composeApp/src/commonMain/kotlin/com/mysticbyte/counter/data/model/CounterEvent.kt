package com.mysticbyte.counter.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CounterEvent(
    val id: String,
    val counterId: String,
    val action: ActionType,
    val previousValue: Int,
    val newValue: Int,
    val amount: Int? = null,
    val metadata: String? = null, // e.g. new name, new step, new color hex
    val timestampMillis: Long = 0L
)
