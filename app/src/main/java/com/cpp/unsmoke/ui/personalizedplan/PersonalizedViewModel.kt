package com.cpp.unsmoke.ui.personalizedplan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PersonalizedViewModel: ViewModel() {
    private val _currentProgress = MutableLiveData<Int>()
    val currentProgress: LiveData<Int> = _currentProgress

    init {
        _currentProgress.value = 20
    }

    fun increaseProgress(){
        _currentProgress.value = _currentProgress.value?.plus(20)
        Log.d("PERSONALIZED_VIEWMODEL", _currentProgress.value.toString())
    }

    fun decreaseProgress(){
        _currentProgress.value = _currentProgress.value?.minus(20)
        Log.d("PERSONALIZED_VIEWMODEL", _currentProgress.value.toString())
    }
}