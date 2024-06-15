package com.cpp.unsmoke.utils.helper.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cpp.unsmoke.di.Injection
import com.cpp.unsmoke.repository.AuthRepository
import com.cpp.unsmoke.repository.JournalRepository
import com.cpp.unsmoke.repository.PersonalizedPlanRepository
import com.cpp.unsmoke.repository.SettingRepository
import com.cpp.unsmoke.repository.ShopRepository
import com.cpp.unsmoke.ui.auth.login.LoginViewModel
import com.cpp.unsmoke.ui.auth.register.RegisterViewModel
import com.cpp.unsmoke.ui.journal.JournalViewModel
import com.cpp.unsmoke.ui.main.profile.ProfileViewModel
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel
import com.cpp.unsmoke.ui.shop.ShopViewModel

class ViewModelFactoryAuth private constructor(
    private val personalizedPlanRepository: PersonalizedPlanRepository,
    private val settingRepository: SettingRepository,
    private val shopRepository: ShopRepository,
    private val journalRepository: JournalRepository
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactoryAuth? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactoryAuth {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactoryAuth(
                    Injection.providePersonalizedPlanRepository(context),
                    Injection.provideSettingRepository(context),
                    Injection.provideShopRepository(context),
                    Injection.provideJournalRepository(context)
                ).also {
                    INSTANCE = it
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            PersonalizedViewModel::class.java -> PersonalizedViewModel(personalizedPlanRepository, settingRepository) as T
            ProfileViewModel::class.java -> ProfileViewModel(settingRepository) as T
            ShopViewModel::class.java -> ShopViewModel(shopRepository, settingRepository) as T
            JournalViewModel::class.java -> JournalViewModel(journalRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

}