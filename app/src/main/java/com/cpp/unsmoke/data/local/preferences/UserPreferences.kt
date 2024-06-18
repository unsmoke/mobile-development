package com.cpp.unsmoke.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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

    suspend fun clearUserPref() {
        dataStore.edit { preferences ->
            preferences[USER_PROV_PREF] = ""
            preferences[USER_CITY_PREF] = ""
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

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}