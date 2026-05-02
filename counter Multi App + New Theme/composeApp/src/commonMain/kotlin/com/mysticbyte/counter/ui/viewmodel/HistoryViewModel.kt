package com.mysticbyte.counter.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysticbyte.counter.data.model.CounterEvent
import com.mysticbyte.counter.data.repository.HistoryRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: HistoryRepository
) : ViewModel() {

    private val _events = MutableStateFlow<List<CounterEvent>>(emptyList())
    val events: StateFlow<List<CounterEvent>> = _events.asStateFlow()

    private var historyJob: Job? = null

    init {
        viewModelScope.launch {
            repository.load()
        }
    }

    fun loadHistoryFor(counterId: String) {
        historyJob?.cancel()
        historyJob = viewModelScope.launch {
            repository.getHistoryFor(counterId).collectLatest { list ->
                _events.value = list
            }
        }
    }

    fun clearHistoryFor(counterId: String) {
        repository.clearHistoryFor(counterId)
    }
}
