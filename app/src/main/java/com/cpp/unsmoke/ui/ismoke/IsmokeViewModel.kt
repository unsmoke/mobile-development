package com.cpp.unsmoke.ui.ismoke

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.userprofile.UserDetailDataResponse
import com.cpp.unsmoke.repository.ActivityRepository
import com.cpp.unsmoke.repository.UserDataRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class IsmokeViewModel(
    private val userDataRepository: UserDataRepository,
    private val activityRepository: ActivityRepository
) : ViewModel() {

    /* FRAGMENT ONE */
    private val _smokeTime = MutableLiveData<String>()
    val smokeTime: LiveData<String> get() = _smokeTime

    private val _cigaretteCount = MutableLiveData<String>()
    val cigaretteCount: LiveData<String> get() = _cigaretteCount

    private val _feelings = MutableLiveData<String>()
    val feelings: LiveData<String> get() = _feelings

    private val _totalCigNow = MutableLiveData<Int>()
    val totalCigNow: LiveData<Int> get() = _totalCigNow

    /* FRAGMENT TWO */

    private val _smokingReason = MutableLiveData<String>()
    val smokingReason: LiveData<String> get() = _smokingReason

    fun setSmokeTime(smokeTime: String) {
        _smokeTime.value = smokeTime
    }

    fun setCigaretteCount(cigaretteCount: String) {
        _cigaretteCount.value = cigaretteCount
    }

    fun setFeelings(feelings: String) {
        _feelings.value = feelings
    }

    fun setTotalCigNow(totalCigNow: Int) {
        _totalCigNow.value = totalCigNow
    }

    fun setSmokingReason(smokingReason: String) {
        _smokingReason.value = smokingReason
    }

    fun setUserCigConsume(cig: Int) {
        viewModelScope.launch {
            userDataRepository.setUserCigConsume(cig)
        }
    }

    fun getUserCigConsumed() = runBlocking {
        userDataRepository.getUserCigarette().first()
    }

    fun setIsmokeIsFilled(isFilled: Boolean) {
        viewModelScope.launch {
            activityRepository.setIsmokeJournalIsFilled(isFilled)
        }
    }

    fun getUserData(): LiveData<Result<UserDetailDataResponse>> {
        return userDataRepository.getUserData()
    }
}