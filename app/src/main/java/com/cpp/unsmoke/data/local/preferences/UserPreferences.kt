package com.cpp.unsmoke.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val dataStore: DataStore<Preferences>) {

    suspend fun setProv(provId: String) {
        dataStore.edit { preferences ->
            preferences[USER_PROV_PREF] = provId
        }
    }

    fun getProv(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[USER_PROV_PREF]
        }
    }

    suspend fun setCity(cityId: String) {
        dataStore.edit { preferences ->
            preferences[USER_CITY_PREF] = cityId
        }
    }

    fun getCity(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[USER_CITY_PREF]
        }
    }

    suspend fun setUserDay(day: String) {
        dataStore.edit { preferences ->
            preferences[USER_DAY_PREF] = day
        }
    }

    fun getUserDay(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[USER_DAY_PREF]
        }
    }

    suspend fun setUserLung(lung: String){
        dataStore.edit { preferences ->
            preferences[USER_LUNG_PREF] = lung
        }
    }

    fun getUserLung(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[USER_LUNG_PREF]
        }
    }

    suspend fun setUserLungId(lung: String){
        dataStore.edit { preferences ->
            preferences[USER_LUNG_ID_PREF] = lung
        }
    }

    fun getUserLungId(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[USER_LUNG_ID_PREF]
        }
    }

    suspend fun setCigaretteConsumed(cigarette: String){
        dataStore.edit { preferences ->
            preferences[CIGARETTE_CONSUMED] = cigarette
        }
    }

    fun getCigaretteConsumed(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[CIGARETTE_CONSUMED]
        }
    }

    suspend fun setJournalIsFilled(isFilled: Boolean) {
        dataStore.edit { preferences ->
            preferences[JOURNAL_IS_FILLED] = isFilled
        }
    }

    fun getJournalIsFilled(): Flow<Boolean?> {
        return dataStore.data.map { preferences ->
            preferences[JOURNAL_IS_FILLED]
        }
    }

    suspend fun setIsmokeJournalIsFilled(isFilled: Boolean) {
        dataStore.edit { preferences ->
            preferences[ISMOKE_JOURNAL_IS_FILLED] = isFilled
        }
    }

    fun getIsmokeJournalIsFilled(): Flow<Boolean?> {
        return dataStore.data.map { preferences ->
            preferences[ISMOKE_JOURNAL_IS_FILLED]
        }
    }

    suspend fun setBreathActivityIsFilled(isFilled: Boolean) {
        dataStore.edit { preferences ->
            preferences[BREATH_ACTIVITY_IS_FILLED] = isFilled
        }
    }

    fun getBreathActivityIsFilled(): Flow<Boolean?> {
        return dataStore.data.map { preferences ->
            preferences[BREATH_ACTIVITY_IS_FILLED]
        }
    }

    suspend fun resetAllActivityIsFilled() {
        dataStore.edit { preferences ->
            preferences[JOURNAL_IS_FILLED] = false
            preferences[ISMOKE_JOURNAL_IS_FILLED] = false
            preferences[BREATH_ACTIVITY_IS_FILLED] = false
        }
    }

    suspend fun clearUserPref() {
        dataStore.edit { preferences ->
            preferences[USER_PROV_PREF] = ""
            preferences[USER_CITY_PREF] = ""
            preferences[USER_DAY_PREF] = ""
            preferences[USER_LUNG_PREF] = ""
            preferences[USER_LUNG_ID_PREF] = ""
            preferences[CIGARETTE_CONSUMED] = ""
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null
        private val USER_PROV_PREF = stringPreferencesKey("user_prov_pref")
        private val USER_CITY_PREF = stringPreferencesKey("user_city_pref")
        private val USER_DAY_PREF = stringPreferencesKey("user_day_pref")
        private val USER_LUNG_PREF = stringPreferencesKey("user_lung_pref")
        private val CIGARETTE_CONSUMED = stringPreferencesKey("cigarette_consumed")
        private val USER_LUNG_ID_PREF = stringPreferencesKey("user_lung_id_pref")
        private val JOURNAL_IS_FILLED = booleanPreferencesKey("journal_is_filled")
        private val ISMOKE_JOURNAL_IS_FILLED = booleanPreferencesKey("ismoke_journal_is_filled")
        private val BREATH_ACTIVITY_IS_FILLED = booleanPreferencesKey("breath_activity_is_filled")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}