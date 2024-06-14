package com.cpp.unsmoke.di

import android.content.Context
import android.util.Log
import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.data.local.preferences.dataStore
import com.cpp.unsmoke.data.remote.retrofit.ApiConfig
import com.cpp.unsmoke.repository.AuthRepository
import com.cpp.unsmoke.repository.PersonalizedPlanRepository
import com.cpp.unsmoke.repository.SettingRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)

        // Step 1: Create temporary ApiService without TokenAuthenticator
        val apiService = ApiConfig.getApiServiceWithoutAuth()

        // Update AuthRepository to use final ApiService
        return AuthRepository.getInstance(apiService, pref)
    }

    fun providePersonalizedPlanRepository(context: Context): PersonalizedPlanRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)

        val userAfter = runBlocking { pref.getToken().first() }

        Log.d("Injection", "providePersonalizedPlanRepository: $userAfter")

        // Create ApiService with AuthRepository and TokenAuthenticator
        val apiService = ApiConfig.getApiService(userAfter ?: "", pref)

        return PersonalizedPlanRepository.getInstance(apiService, pref)
    }

    fun provideSettingRepository(context: Context): SettingRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)
        return SettingRepository.getInstance(pref)
    }
}