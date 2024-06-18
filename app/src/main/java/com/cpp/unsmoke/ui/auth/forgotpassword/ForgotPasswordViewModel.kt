package com.cpp.unsmoke.ui.auth.forgotpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.auth.ForgotPasswordResponse
import com.cpp.unsmoke.repository.AuthRepository

class ForgotPasswordViewModel(private val authRepository: AuthRepository): ViewModel(){
    fun forgotPassword(email: String): LiveData<Result<ForgotPasswordResponse>>{
        return authRepository.forgotPassword(email)
    }
}