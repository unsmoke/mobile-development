package com.cpp.unsmoke.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

class GamificationPreferences(private val dataStore: DataStore<Preferences>) {

    suspend fun setDailyJournalDone(isDone: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DAILY_JOURNAL_DONE] = isDone
        }
    }

    fun getDailyJournalDone() = dataStore.data.map { preferences ->
        preferences[IS_DAILY_JOURNAL_DONE] ?: false
    }

    suspend fun setSmokeUnderLimits(isUnderLimits: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_SMOKE_UNDER_LIMITS] = isUnderLimits
        }
    }

    fun getSmokeUnderLimits() = dataStore.data.map { preferences ->
        preferences[IS_SMOKE_UNDER_LIMITS] ?: false
    }

    suspend fun setExerciseBreathDone(isDone: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_EXERCISE_BREATH_DONE] = isDone
        }
    }

    fun getExerciseBreathDone() = dataStore.data.map { preferences ->
        preferences[IS_EXERCISE_BREATH_DONE] ?: false
    }

    suspend fun resetGamification() {
        dataStore.edit { preferences ->
            preferences[IS_DAILY_JOURNAL_DONE] = false
            preferences[IS_SMOKE_UNDER_LIMITS] = false
            preferences[IS_EXERCISE_BREATH_DONE] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: GamificationPreferences? = null
        private val IS_DAILY_JOURNAL_DONE = booleanPreferencesKey("is_daily_journal_done")
        private val IS_SMOKE_UNDER_LIMITS = booleanPreferencesKey("is_smoke_under_limits")
        private val IS_EXERCISE_BREATH_DONE = booleanPreferencesKey("is_exercise_breath_done")

        fun getInstance(dataStore: DataStore<Preferences>): GamificationPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = GamificationPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}