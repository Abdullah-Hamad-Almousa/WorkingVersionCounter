package com.mysticbyte.counter.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysticbyte.counter.data.model.Counter
import com.mysticbyte.counter.data.repository.CountersRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CountersViewModel(
    private val repository: CountersRepository
) : ViewModel() {

    val counters: StateFlow<List<Counter>> = repository.counters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.load()
        }
    }

    fun addCounter(name: String, startValue: Int, step: Int, colorHex: String? = null): String {
        return repository.addCounter(name, startValue, step, colorHex).id
    }

    fun deleteCounter(id: String) {
        repository.deleteCounter(id)
    }

    fun increment(id: String) {
        repository.increment(id)
    }

    fun decrement(id: String) {
        repository.decrement(id)
    }

    fun reset(id: String) {
        repository.reset(id)
    }

    fun addToValue(id: String, amount: Int) {
        repository.addToValue(id, amount)
    }

    fun setValue(id: String, newValue: Int) {
        repository.setValue(id, newValue)
    }

    fun updateName(id: String, newName: String) {
        repository.getCounter(id)?.let {
            repository.updateCounter(it.copy(name = newName.trim().ifEmpty { it.name }))
        }
    }

    fun updateStep(id: String, newStep: Int) {
        repository.getCounter(id)?.let {
            repository.updateCounter(it.copy(step = newStep.coerceAtLeast(1)))
        }
    }

    fun updateColor(id: String, colorHex: String?) {
        repository.getCounter(id)?.let {
            repository.updateCounter(it.copy(colorHex = colorHex))
        }
    }

    fun getCounter(id: String): Counter? = repository.getCounter(id)
}
