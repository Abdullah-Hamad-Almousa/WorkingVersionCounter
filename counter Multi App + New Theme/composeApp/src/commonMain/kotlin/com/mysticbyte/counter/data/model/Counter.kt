package com.mysticbyte.counter.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Counter(
    val id: String,
    val name: String,
    val value: Int = 0,
    val step: Int = 1,
    val colorHex: String? = null,
    val createdAtMillis: Long = 0L
)
