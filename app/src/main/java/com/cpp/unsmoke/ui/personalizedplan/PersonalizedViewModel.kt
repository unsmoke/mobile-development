package com.cpp.unsmoke.ui.personalizedplan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PersonalizedViewModel: ViewModel() {
    private val _currentProgress = MutableLiveData<Int>()
    val currentProgress: LiveData<Int> get() = _currentProgress

    private val _dateOfBirth = MutableLiveData<String>()
    val dateOfBirth: LiveData<String> get() = _dateOfBirth

    private val _province = MutableLiveData<String>()
    val province: LiveData<String> get() = _province

    private val _city = MutableLiveData<String>()
    val city: LiveData<String> get() = _city

    init {
        _currentProgress.value = 10
    }

    fun increaseProgress() {
        _currentProgress.value = _currentProgress.value?.plus(10)
        Log.d("PERSONALIZED_VIEWMODEL", _currentProgress.value.toString())
    }

    fun decreaseProgress() {
        _currentProgress.value = _currentProgress.value?.minus(10)
        Log.d("PERSONALIZED_VIEWMODEL", _currentProgress.value.toString())
    }

    fun setDateOfBirth(date: String) {
        _dateOfBirth.value = date
    }

    fun setProvince(provinceName: String) {
        _province.value = provinceName
    }

    fun setCity(cityName: String) {
        _city.value = cityName
    }
}
