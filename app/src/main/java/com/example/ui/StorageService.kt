package com.example.ui

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "math_riddles_settings")

class StorageService(private val context: Context) {

    companion object {
        val CURRENT_LEVEL = intPreferencesKey("current_level")
        val GAME_COMPLETED = booleanPreferencesKey("game_completed")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
    }

    val currentLevel: Flow<Int> = context.dataStore.data.map { it[CURRENT_LEVEL] ?: 0 }
    val gameCompleted: Flow<Boolean> = context.dataStore.data.map { it[GAME_COMPLETED] ?: false }
    val soundEnabled: Flow<Boolean> = context.dataStore.data.map { it[SOUND_ENABLED] ?: true }
    val vibrationEnabled: Flow<Boolean> = context.dataStore.data.map { it[VIBRATION_ENABLED] ?: true }

    suspend fun saveCurrentLevel(level: Int) {
        context.dataStore.edit { it[CURRENT_LEVEL] = level }
    }

    suspend fun saveGameCompleted(completed: Boolean) {
        context.dataStore.edit { it[GAME_COMPLETED] = completed }
    }

    suspend fun toggleSound() {
        context.dataStore.edit {
            val current = it[SOUND_ENABLED] ?: true
            it[SOUND_ENABLED] = !current
        }
    }

    suspend fun toggleVibration() {
        context.dataStore.edit {
            val current = it[VIBRATION_ENABLED] ?: true
            it[VIBRATION_ENABLED] = !current
        }
    }

    suspend fun resetProgress() {
        context.dataStore.edit {
            it[CURRENT_LEVEL] = 0
            it[GAME_COMPLETED] = false
        }
    }
}
