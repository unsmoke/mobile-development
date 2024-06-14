package com.cpp.unsmoke.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpp.unsmoke.repository.SettingRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val settingRepository: SettingRepository): ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is profile Fragment"
    }

    val text: LiveData<String> = _text

    fun logout() {
        viewModelScope.launch {
            settingRepository.logout()
        }
    }
}