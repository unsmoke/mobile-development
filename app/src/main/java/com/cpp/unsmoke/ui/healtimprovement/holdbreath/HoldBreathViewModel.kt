package com.cpp.unsmoke.ui.healtimprovement.holdbreath

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.activity.BreathResponse
import com.cpp.unsmoke.repository.ActivityRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HoldBreathViewModel(
    private val activityRepository: ActivityRepository
): ViewModel(){

    private val _duration = MutableLiveData<Int>()
    val duration: LiveData<Int> get() = _duration

    private val _highestRecord = MutableLiveData<Int>()
    val highestRecord: LiveData<Int> get() = _highestRecord

    fun sendBreathData(duration: Int) {
        _duration.value = duration
    }

    fun setHighestRecord(highestRecord: Int) {
        _highestRecord.value = highestRecord
    }

    fun sendBreathDataToRepository(duration: Int): LiveData<Result<BreathResponse>> {
        return activityRepository.sendBreathData(duration)
    }

    fun setBreathIsFilled() {
        viewModelScope.launch {
            activityRepository.setBreathActivityIsFilled(true)
        }
    }

    fun getBreathIsFilled(): Boolean? {
        return runBlocking {
            activityRepository.getBreathActivityIsFilled().first()
        }
    }
}