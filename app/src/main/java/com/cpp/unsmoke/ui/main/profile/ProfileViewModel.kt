package com.cpp.unsmoke.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.userprofile.UserDetailDataResponse
import com.cpp.unsmoke.repository.SettingRepository
import com.cpp.unsmoke.repository.UserDataRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProfileViewModel(
    private val userDataRepository: UserDataRepository,
    private val settingRepository: SettingRepository
) : ViewModel() {
    fun getUserData(): LiveData<Result<UserDetailDataResponse>> {
        return userDataRepository.getUserData()
    }

    fun logout() {
        runBlocking {
            settingRepository.logout()
            settingRepository.clearUserPreferences()
        }
    }
}