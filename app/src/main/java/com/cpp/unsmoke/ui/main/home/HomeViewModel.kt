package com.cpp.unsmoke.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.userprofile.UserDetailDataResponse
import com.cpp.unsmoke.repository.SettingRepository
import com.cpp.unsmoke.repository.ShopRepository
import com.cpp.unsmoke.repository.UserDataRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeViewModel(
    private val userDataRepository: UserDataRepository,
    private val settingRepository: SettingRepository
) : ViewModel() {

    private val _currentLungUrl = MutableLiveData<String>()
    val currentLungUrl: LiveData<String> get() = _currentLungUrl

    fun loadLungUrl() {
        viewModelScope.launch {
            _currentLungUrl.value = userDataRepository.getLungUrl().first()
        }
    }

    fun getUserData(): LiveData<Result<UserDetailDataResponse>> {
        return userDataRepository.getUserData()
    }
}