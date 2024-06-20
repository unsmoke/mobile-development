package com.cpp.unsmoke.di

import LeaderboardRepository
import android.content.Context
import com.cpp.unsmoke.data.local.preferences.GamificationPreferences
import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.data.local.preferences.UserPreferences
import com.cpp.unsmoke.data.local.preferences.dataStore
import com.cpp.unsmoke.data.remote.retrofit.ApiConfig
import com.cpp.unsmoke.repository.ActivityRepository
import com.cpp.unsmoke.repository.AuthRepository
import com.cpp.unsmoke.repository.JournalRepository
import com.cpp.unsmoke.repository.MilestoneRepository
import com.cpp.unsmoke.repository.PersonalizedPlanRepository
import com.cpp.unsmoke.repository.SettingRepository
import com.cpp.unsmoke.repository.ShopRepository
import com.cpp.unsmoke.repository.UserDataRepository

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)

        val apiService = ApiConfig.getApiServiceWithoutAuth(pref)

        return AuthRepository.getInstance(apiService, pref)
    }

    fun providePersonalizedPlanRepository(context: Context): PersonalizedPlanRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)

        val userPref = UserPreferences.getInstance(context.dataStore)

        val apiService = ApiConfig.getApiServiceWithoutAuth(pref)

        return PersonalizedPlanRepository.getInstance(apiService, pref, userPref)
    }

    fun provideShopRepository(context: Context): ShopRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)

        val apiService = ApiConfig.getApiServiceWithoutAuth(pref)

        val userPref = UserPreferences.getInstance(context.dataStore)

        return ShopRepository.getInstance(apiService, pref, userPref)
    }

    fun provideJournalRepository(context: Context): JournalRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)

        val apiService = ApiConfig.getApiServiceWithoutAuth(pref)

        return JournalRepository.getInstance(apiService, pref)
    }

    fun provideLeaderboardRepository(context: Context): LeaderboardRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)

        val userPref = UserPreferences.getInstance(context.dataStore)

        val apiService = ApiConfig.getApiServiceWithoutAuth(pref)

        return LeaderboardRepository.getInstance(apiService, pref, userPref)
    }

    fun provideGamificationPreferences(context: Context): GamificationPreferences {
        return GamificationPreferences.getInstance(context.dataStore)
    }

    fun provideUserDataRepository(context: Context): UserDataRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)

        val userPref = UserPreferences.getInstance(context.dataStore)

        val apiService = ApiConfig.getApiServiceWithoutAuth(pref)

        return UserDataRepository.getInstance(apiService, pref, userPref)
    }

    fun provideMilestoneRepository(context: Context): MilestoneRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)

        val apiService = ApiConfig.getApiServiceWithoutAuth(pref)

        return MilestoneRepository.getInstance(apiService, pref)
    }

    fun provideActivityRepository(context: Context): ActivityRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)

        val userPref = UserPreferences.getInstance(context.dataStore)

        val apiService = ApiConfig.getApiServiceWithoutAuth(pref)

        return ActivityRepository.getInstance(apiService, userPref, pref)
    }

    fun provideSettingRepository(context: Context): SettingRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)

        val userPref = UserPreferences.getInstance(context.dataStore)


        return SettingRepository.getInstance(pref, userPref)
    }
}