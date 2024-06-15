package com.cpp.unsmoke.repository

import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.data.remote.retrofit.ApiService

class JournalRepository(
    private var apiService: ApiService,
    private val loginPreferences: LoginPreferences
) {

    companion object {
        @Volatile
        private var instance: JournalRepository? = null

        fun getInstance(
            apiService: ApiService,
            preferences: LoginPreferences
        ): JournalRepository =
            instance ?: synchronized(this) {
                instance ?: JournalRepository(apiService, preferences).also {
                    instance = it
                }
            }
    }
}