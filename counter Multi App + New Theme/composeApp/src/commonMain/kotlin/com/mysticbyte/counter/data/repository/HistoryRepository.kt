package com.mysticbyte.counter.data.repository

import com.mysticbyte.counter.data.local.DataStoreManager
import com.mysticbyte.counter.data.model.ActionType
import com.mysticbyte.counter.data.model.CounterEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class HistoryRepository(
    private val dataStoreManager: DataStoreManager,
    private val scope: CoroutineScope
) {

    private val _events = MutableStateFlow<List<CounterEvent>>(emptyList())
    val events: StateFlow<List<CounterEvent>> = _events.asStateFlow()

    suspend fun load() {
        val loaded = dataStoreManager.loadHistory()
        _events.value = loaded
    }

    fun logEvent(
        counterId: String,
        action: ActionType,
        previousValue: Int,
        newValue: Int,
        amount: Int? = null,
        metadata: String? = null
    ) {
        val event = CounterEvent(
            id = generateId(),
            counterId = counterId,
            action = action,
            previousValue = previousValue,
            newValue = newValue,
            amount = amount,
            metadata = metadata,
            timestampMillis = currentEpochMillis()
        )
        _events.update { current -> current + event }
        persist()
    }

    fun getHistoryFor(counterId: String): Flow<List<CounterEvent>> {
        return _events.map { list ->
            list.filter { it.counterId == counterId }
                .sortedByDescending { it.timestampMillis }
        }
    }

    fun clearHistoryFor(counterId: String) {
        _events.update { current -> current.filterNot { it.counterId == counterId } }
        persist()
    }

    private fun persist() {
        scope.launch {
            dataStoreManager.saveHistory(_events.value)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun generateId(): String = Uuid.random().toString()

    private fun currentEpochMillis(): Long {
        return kotlin.time.Clock.System.now().toEpochMilliseconds()
    }
}
