package com.cpp.unsmoke.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.RegisterResponse
import com.cpp.unsmoke.repository.AuthRepository

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun register(
        fullName: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>>{
        return authRepository.register(fullName, email, password)
    }
}