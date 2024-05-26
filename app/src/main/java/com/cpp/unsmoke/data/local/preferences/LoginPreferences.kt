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

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login_preferences")

class LoginPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val loginToken = stringPreferencesKey("token")
    private val loginStatus = booleanPreferencesKey("status")
    private val refreshToken = stringPreferencesKey("refresh")

    // save auth token and refresh token
    suspend fun saveToken(token: String, refresh: String) {
        dataStore.edit { preferences ->
            preferences[loginToken] = token
            preferences[refreshToken] = refresh
        }
    }

    // set login status
    suspend fun loginPref() {
        dataStore.edit { preferences ->
            preferences[loginStatus] = true
        }
    }

    // get login status
    fun getLoginStatus(): Flow<Boolean?> {
        return dataStore.data.map { preferences ->
            preferences[loginStatus]
        }
    }

    // get auth token
    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[loginToken]
        }
    }

    // get token for refresh
    fun getRefreshToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[refreshToken]
        }
    }

    //update token
    suspend fun updateToken(token: String){
        dataStore.edit { preference ->
            preference[loginToken] = token
        }
    }

    // do logout
    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[loginStatus] = false
            preferences[loginToken] = ""
            preferences[refreshToken] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): LoginPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = LoginPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}