package com.cpp.unsmoke.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.auth.RegisterResponse
import com.cpp.unsmoke.data.remote.responses.auth.VerifyResponse
import com.cpp.unsmoke.repository.AuthRepository

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    fun setEmail(email: String){
        _email.value = email
    }

    fun register(
        fullName: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>>{
        return authRepository.register(fullName, email, password)
    }

    fun verifyEmail(
        email: String,
        otp: String
    ): LiveData<Result<VerifyResponse>>{
        return authRepository.verifyOtpRegister(email, otp)
    }
}