package com.cpp.unsmoke.repository

import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.data.remote.retrofit.ApiService

class UserDataRepository(
    private val apiService: ApiService,
    private val preferences: LoginPreferences
) {

    companion object {
        @Volatile
        private var instance: UserDataRepository? = null

        fun getInstance(
            apiService: ApiService,
            preferences: LoginPreferences
        ): UserDataRepository =
            instance ?: synchronized(this) {
                instance ?: UserDataRepository(apiService, preferences).also {
                    instance = it
                }
            }
    }
}