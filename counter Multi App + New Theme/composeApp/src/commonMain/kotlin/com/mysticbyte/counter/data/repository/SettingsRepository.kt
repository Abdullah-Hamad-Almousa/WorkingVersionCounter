package com.mysticbyte.counter.data.repository

import com.mysticbyte.counter.data.local.DataStoreManager
import com.mysticbyte.counter.data.model.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsRepository(
    private val dataStoreManager: DataStoreManager,
    private val scope: CoroutineScope
) {
    private val _theme = MutableStateFlow(AppTheme.LIGHT)
    val theme: StateFlow<AppTheme> = _theme.asStateFlow()

    suspend fun load() {
        val loaded = dataStoreManager.loadTheme()
        _theme.value = loaded
    }

    fun setTheme(theme: AppTheme) {
        _theme.value = theme
        persist()
    }

    private fun persist() {
        scope.launch {
            dataStoreManager.saveTheme(_theme.value)
        }
    }
}
