package com.cpp.unsmoke.repository

import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.data.remote.retrofit.ApiService

class LeaderboardRepository(
    private var apiService: ApiService,
    private val loginPreferences: LoginPreferences
) {

    fun getLeaderboard() {

    }
}