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

    suspend fun clearLanguage() {
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

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}