package com.cpp.unsmoke.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.auth.LoginResponse
import com.cpp.unsmoke.repository.AuthRepository

class LoginViewModel(private val authRepos: AuthRepository) : ViewModel() {
    fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
        return authRepos.login(email, password)
    }
}