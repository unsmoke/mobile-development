package com.cpp.unsmoke.repository

import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.data.local.preferences.UserPreferences
import com.cpp.unsmoke.data.remote.retrofit.ApiService

class SettingRepository(
    private val loginPreferences: LoginPreferences,
    private val userPreferences: UserPreferences
) {
    suspend fun clearUserPreferences() = userPreferences.clearUserPref()

    suspend fun logout() = loginPreferences.logout()

    fun getLoginStatus() = loginPreferences.getLoginStatus()

    companion object {
        @Volatile
        private var instance: SettingRepository? = null

        fun getInstance(
            preferences: LoginPreferences,
            userPreferences: UserPreferences
        ): SettingRepository=
            instance ?: synchronized(this) {
                instance ?: SettingRepository(preferences, userPreferences).also {
                    instance = it
                }
            }
    }
}