package com.cpp.unsmoke.ui.auth.newpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.auth.ResetPasswordResponse
import com.cpp.unsmoke.repository.AuthRepository

class NewPasswordViewModel(private val authRepository: AuthRepository): ViewModel() {
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    fun setEmail(email: String){
        _email.value = email
    }

    fun newPassword(email: String, password: String, code: String): LiveData<Result<ResetPasswordResponse>>{
        return authRepository.resetPassword(email, code, password)
    }
}