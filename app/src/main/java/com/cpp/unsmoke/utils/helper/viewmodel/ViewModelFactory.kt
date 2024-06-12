package com.cpp.unsmoke.utils.helper.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cpp.unsmoke.di.Injection
import com.cpp.unsmoke.repository.AuthRepository
import com.cpp.unsmoke.repository.PersonalizedPlanRepository
import com.cpp.unsmoke.ui.auth.login.LoginViewModel
import com.cpp.unsmoke.ui.auth.register.RegisterViewModel
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository,
    private val personalizedPlanRepository: PersonalizedPlanRepository
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideAuthRepository(context),
                    Injection.providePersonalizedPlanRepository(context)
                ).also {
                    INSTANCE = it
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(PersonalizedViewModel::class.java)){
            return PersonalizedViewModel(personalizedPlanRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}