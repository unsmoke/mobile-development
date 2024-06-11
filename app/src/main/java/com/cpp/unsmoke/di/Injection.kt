package com.cpp.unsmoke.di

import android.content.Context
import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.data.local.preferences.dataStore
import com.cpp.unsmoke.data.remote.retrofit.ApiConfig
import com.cpp.unsmoke.repository.AuthRepository
import com.cpp.unsmoke.repository.PersonalizedPlanRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideAuthRepository(
        context: Context
    ): AuthRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getToken().first() }
        val apiService = ApiConfig.getApiService(user.toString())
        return AuthRepository.getInstance(apiService, pref)
    }

    fun providePersonalizedPlanRepository(
        context: Context
    ): PersonalizedPlanRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getToken().first() }
        val apiService = ApiConfig.getApiService(user.toString())
        return PersonalizedPlanRepository.getInstance(apiService, pref)
    }
}