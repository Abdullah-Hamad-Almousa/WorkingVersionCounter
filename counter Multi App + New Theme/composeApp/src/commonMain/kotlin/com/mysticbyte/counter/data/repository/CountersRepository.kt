package com.mysticbyte.counter.data.repository

import com.mysticbyte.counter.data.local.DataStoreManager
import com.mysticbyte.counter.data.model.ActionType
import com.mysticbyte.counter.data.model.Counter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class CountersRepository(
    private val historyRepository: HistoryRepository,
    private val dataStoreManager: DataStoreManager,
    private val scope: CoroutineScope
) {

    private val _counters = MutableStateFlow<List<Counter>>(emptyList())
    val counters: StateFlow<List<Counter>> = _counters.asStateFlow()

    suspend fun load() {
        val loaded = dataStoreManager.loadCounters()
        _counters.value = loaded
    }

    fun addCounter(
        name: String,
        startValue: Int,
        step: Int,
        colorHex: String? = null
    ): Counter {
        val counter = Counter(
            id = generateId(),
            name = name.trim().ifEmpty { "Counter" },
            value = startValue,
            step = step.coerceAtLeast(1),
            colorHex = colorHex,
            createdAtMillis = kotlin.time.Clock.System.now().toEpochMilliseconds()
        )
        _counters.update { current -> current + counter }
        historyRepository.logEvent(
            counterId = counter.id,
            action = ActionType.CREATED,
            previousValue = 0,
            newValue = startValue,
            metadata = counter.name
        )
        persist()
        return counter
    }

    fun deleteCounter(id: String) {
        _counters.update { current -> current.filter { it.id != id } }
        historyRepository.clearHistoryFor(id)
        persist()
    }

    fun updateCounter(counter: Counter) {
        val prev = getCounter(counter.id)
        _counters.update { current ->
            current.map { if (it.id == counter.id) counter else it }
        }
        if (prev != null) {
            when {
                prev.name != counter.name -> historyRepository.logEvent(
                    counterId = counter.id,
                    action = ActionType.NAME_CHANGE,
                    previousValue = counter.value,
                    newValue = counter.value,
                    metadata = counter.name
                )
                prev.step != counter.step -> historyRepository.logEvent(
                    counterId = counter.id,
                    action = ActionType.STEP_CHANGE,
                    previousValue = counter.value,
                    newValue = counter.value,
                    metadata = counter.step.toString()
                )
                prev.colorHex != counter.colorHex -> historyRepository.logEvent(
                    counterId = counter.id,
                    action = ActionType.COLOR_CHANGE,
                    previousValue = counter.value,
                    newValue = counter.value,
                    metadata = counter.colorHex
                )
                else -> { /* other mutations handled separately */ }
            }
        }
        persist()
    }

    fun increment(id: String) {
        val prevValue = getCounter(id)?.value ?: return
        _counters.update { current ->
            current.map {
                if (it.id == id) {
                    val newValue = it.value + it.step
                    historyRepository.logEvent(
                        counterId = id,
                        action = ActionType.INCREMENT,
                        previousValue = prevValue,
                        newValue = newValue,
                        amount = it.step
                    )
                    it.copy(value = newValue)
                } else it
            }
        }
        persist()
    }

    fun decrement(id: String) {
        val prevValue = getCounter(id)?.value ?: return
        _counters.update { current ->
            current.map {
                if (it.id == id) {
                    val newValue = it.value - it.step
                    historyRepository.logEvent(
                        counterId = id,
                        action = ActionType.DECREMENT,
                        previousValue = prevValue,
                        newValue = newValue,
                        amount = it.step
                    )
                    it.copy(value = newValue)
                } else it
            }
        }
        persist()
    }

    fun reset(id: String) {
        val prevValue = getCounter(id)?.value ?: return
        _counters.update { current ->
            current.map {
                if (it.id == id) {
                    historyRepository.logEvent(
                        counterId = id,
                        action = ActionType.RESET,
                        previousValue = prevValue,
                        newValue = 0
                    )
                    it.copy(value = 0)
                } else it
            }
        }
        persist()
    }

    fun addToValue(id: String, amount: Int) {
        val prevValue = getCounter(id)?.value ?: return
        val newValue = prevValue + amount
        _counters.update { current ->
            current.map {
                if (it.id == id) {
                    historyRepository.logEvent(
                        counterId = id,
                        action = ActionType.ADD,
                        previousValue = prevValue,
                        newValue = newValue,
                        amount = amount
                    )
                    it.copy(value = newValue)
                } else it
            }
        }
        persist()
    }

    fun setValue(id: String, newValue: Int) {
        val prevValue = getCounter(id)?.value ?: return
        _counters.update { current ->
            current.map {
                if (it.id == id) {
                    historyRepository.logEvent(
                        counterId = id,
                        action = ActionType.SET,
                        previousValue = prevValue,
                        newValue = newValue,
                        amount = newValue
                    )
                    it.copy(value = newValue)
                } else it
            }
        }
        persist()
    }

    fun getCounter(id: String): Counter? {
        return _counters.value.find { it.id == id }
    }

    private fun persist() {
        scope.launch {
            dataStoreManager.saveCounters(_counters.value)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun generateId(): String = Uuid.random().toString()
}
