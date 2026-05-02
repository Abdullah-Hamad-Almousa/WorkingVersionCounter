package com.mysticbyte.counter.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysticbyte.counter.data.model.AppTheme
import com.mysticbyte.counter.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {

    val theme: StateFlow<AppTheme> = repository.theme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppTheme.LIGHT)

    init {
        viewModelScope.launch {
            repository.load()
        }
    }

    fun setTheme(theme: AppTheme) {
        repository.setTheme(theme)
    }
}
