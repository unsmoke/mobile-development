package com.cpp.unsmoke.ui.ismoke

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IsmokeViewModel(): ViewModel(){

    /* FRAGMENT ONE */
    private val _smokeTime = MutableLiveData<String>()
    val smokeTime: LiveData<String> get() = _smokeTime

    private val _cigaretteCount = MutableLiveData<String>()
    val cigaretteCount: LiveData<String> get() = _cigaretteCount

    private val _feelings = MutableLiveData<String>()
    val feelings: LiveData<String> get() = _feelings

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

    fun setSmokingReason(smokingReason: String) {
        _smokingReason.value = smokingReason
    }
}