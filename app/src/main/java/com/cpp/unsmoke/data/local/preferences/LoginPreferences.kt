package com.cpp.unsmoke.data.local.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login_preferences")

class LoginPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val loginToken = stringPreferencesKey("token")
    private val loginStatus = booleanPreferencesKey("status")
    private val refreshToken = stringPreferencesKey("refresh")
    private val userId = stringPreferencesKey("userid")

    // save auth token and refresh token
    suspend fun saveToken(token: String, refresh: String) {
        Log.d("LOGIN_PREFERENCES", "save token dipanggil: $token, $refresh")
        dataStore.edit { preferences ->
            preferences[loginToken] = token
            preferences[refreshToken] = refresh
        }
    }

    // save user id
    suspend fun saveUserId(id: String) {
        dataStore.edit { preferences ->
            preferences[userId] = id
        }
    }

    // set login status
    suspend fun loginPref() {
        dataStore.edit { preferences ->
            preferences[loginStatus] = true
        }
    }

    // get login status
    fun getLoginStatus(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[loginStatus] ?: false
            }
    }

    // get auth token
    fun getToken(): Flow<String?> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[loginToken]
            }
    }

    // get token for refresh
    fun getRefreshToken(): Flow<String?> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[refreshToken]
            }
    }

    // get user id
    fun getUserId(): Flow<String?> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[userId]
            }
    }

    // update token
    suspend fun updateToken(token: String) {
        dataStore.edit { preferences ->
            preferences[loginToken] = token
        }
    }

    // do logout
    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[loginStatus] = false
            preferences[loginToken] = ""
            preferences[refreshToken] = ""
            preferences[userId] = ""
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
