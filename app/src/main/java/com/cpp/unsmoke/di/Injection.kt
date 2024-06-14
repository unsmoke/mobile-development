package com.cpp.unsmoke.di

import android.content.Context
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
        val user = runBlocking { pref.getToken().first() }

        // Step 1: Create temporary ApiService without TokenAuthenticator
        val tempApiService = ApiConfig.getApiServiceWithoutAuth()

        // Step 2: Create AuthRepository with temporary ApiService
        val authRepository = AuthRepository.getInstance(tempApiService, pref)

        // Step 3: Create final ApiService with TokenAuthenticator
        val apiService = ApiConfig.getApiService(user ?: "", authRepository, pref)

        // Update AuthRepository to use final ApiService
        return AuthRepository.getInstance(apiService, pref)
    }

    fun providePersonalizedPlanRepository(context: Context): PersonalizedPlanRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getToken().first() }

        // Use the same method to get AuthRepository
        val authRepository = provideAuthRepository(context)

        // Create ApiService with AuthRepository and TokenAuthenticator
        val apiService = ApiConfig.getApiService(user ?: "", authRepository, pref)

        return PersonalizedPlanRepository.getInstance(apiService, pref)
    }

    fun provideSettingRepository(context: Context): SettingRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)
        return SettingRepository.getInstance(pref)
    }
}