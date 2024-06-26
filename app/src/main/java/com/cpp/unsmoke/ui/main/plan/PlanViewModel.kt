package com.cpp.unsmoke.ui.main.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.milestone.MilestoneResponse
import com.cpp.unsmoke.data.remote.responses.userprofile.UserDetailDataResponse
import com.cpp.unsmoke.repository.MilestoneRepository
import com.cpp.unsmoke.repository.SettingRepository
import com.cpp.unsmoke.repository.UserDataRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlanViewModel(
    private val milestoneRepository: MilestoneRepository,
    private val userDataRepository: UserDataRepository,
    private val settingRepository: SettingRepository
): ViewModel() {
    private val _currentLungUrl = MutableLiveData<String>()
    val currentLungUrl: LiveData<String> get() = _currentLungUrl

    fun loadLungUrl() {
        viewModelScope.launch {
            _currentLungUrl.value = userDataRepository.getLungUrl().first() ?: "https://storage.googleapis.com/unsmoke-assets/lungs/plain-lung.svg"
        }
    }

    fun getUserData(): LiveData<Result<UserDetailDataResponse>> {
        return userDataRepository.getUserData()
    }

    fun getAllMilestones(): LiveData<Result<MilestoneResponse>> {
        return milestoneRepository.getAllMilestones()
    }

    fun getMilestoneById(): LiveData<Result<MilestoneResponse>> {
        return milestoneRepository.getMilestoneById()
    }
}