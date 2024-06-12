package com.cpp.unsmoke.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.auth.LoginResponse
import com.cpp.unsmoke.data.remote.responses.personalized.GetPersonalizedResponse
import com.cpp.unsmoke.repository.AuthRepository
import com.cpp.unsmoke.repository.PersonalizedPlanRepository

class LoginViewModel(private val authRepos: AuthRepository, private val personalizedPlanRepository: PersonalizedPlanRepository) : ViewModel() {
    fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
        return authRepos.login(email, password)
    }

    fun getPersonalizedPlan(): LiveData<Result<GetPersonalizedResponse>> {
        return personalizedPlanRepository.getPersonalizedPlan()
    }
}