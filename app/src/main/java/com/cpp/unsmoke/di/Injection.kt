package com.cpp.unsmoke.di

import android.content.Context
import android.util.Log
import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.data.local.preferences.dataStore
import com.cpp.unsmoke.data.remote.retrofit.ApiConfig
import com.cpp.unsmoke.repository.AuthRepository
import com.cpp.unsmoke.repository.JournalRepository
import com.cpp.unsmoke.repository.PersonalizedPlanRepository
import com.cpp.unsmoke.repository.SettingRepository
import com.cpp.unsmoke.repository.ShopRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)

        val apiService = ApiConfig.getApiServiceWithoutAuth(pref)

        return AuthRepository.getInstance(apiService, pref)
    }

    fun providePersonalizedPlanRepository(context: Context): PersonalizedPlanRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)

        val apiService = ApiConfig.getApiServiceWithoutAuth(pref)

        return PersonalizedPlanRepository.getInstance(apiService, pref)
    }

    fun provideShopRepository(context: Context): ShopRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)

        val apiService = ApiConfig.getApiServiceWithoutAuth(pref)

        return ShopRepository.getInstance(apiService, pref)
    }

    fun provideJournalRepository(context: Context): JournalRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)

        val apiService = ApiConfig.getApiServiceWithoutAuth(pref)

        return JournalRepository.getInstance(apiService, pref)
    }

    fun provideSettingRepository(context: Context): SettingRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)
        return SettingRepository.getInstance(pref)
    }
}