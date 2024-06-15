package com.cpp.unsmoke.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.auth.LoginResponse
import com.cpp.unsmoke.data.remote.responses.personalized.GetPersonalizedResponse
import com.cpp.unsmoke.repository.AuthRepository
import com.cpp.unsmoke.repository.PersonalizedPlanRepository
import com.cpp.unsmoke.repository.SettingRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepos: AuthRepository,
    private val settingRepository: SettingRepository
) : ViewModel() {
    fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
        return authRepos.login(email, password)
    }

    fun getLoginStatus() = authRepos.getLoginStatus()

    fun logout() {
        viewModelScope.launch {
            settingRepository.logout()
        }
    }
}