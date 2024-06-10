package com.cpp.unsmoke.ui.personalizedplan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PersonalizedViewModel: ViewModel() {
    /* TOOLBAR ANIMATION */

    private val _currentProgress = MutableLiveData<Int>()
    val currentProgress: LiveData<Int> get() = _currentProgress

    /* FRAGMENT ONE */

    private val _dateOfBirth = MutableLiveData<String>()
    val dateOfBirth: LiveData<String> get() = _dateOfBirth

    private val _province = MutableLiveData<String>()
    val province: LiveData<String> get() = _province

    private val _city = MutableLiveData<String>()
    val city: LiveData<String> get() = _city

    /* FRAGMENT TWO */

    private val _gender = MutableLiveData<String>()
    val gender: LiveData<String> get() = _gender

    /* FRAGMENT THREE */

    private val _firstSmokeDate = MutableLiveData<String>()
    val firstSmokeDate: LiveData<String> get() = _firstSmokeDate

    /* FRAGMENT FOUR */

    private val _cigarettesPerDay = MutableLiveData<Int>()
    val cigarettesPerDay: LiveData<Int> get() = _cigarettesPerDay

    private val _cigarettesPerPack = MutableLiveData<Int>()
    val cigarettesPerPack: LiveData<Int> get() = _cigarettesPerPack

    private val _packPrice = MutableLiveData<Float>()
    val packPrice: LiveData<Float> get() = _packPrice

    /* FRAGMENT FIVE */

    private val _isNicotineMed = MutableLiveData<Boolean>()
    val isNicotineMed: LiveData<Boolean> get() = _isNicotineMed

    /* FRAGMENT SIX */

    private val _isUsingECigarette = MutableLiveData<Int>()
    val isUsingECigarette: LiveData<Int> get() = _isUsingECigarette

    /* FRAGMENT SEVEN */

    private val _isUsingOtherTobacco = MutableLiveData<Int>()
    val isUsingOtherTobacco: LiveData<Int> get() = _isUsingOtherTobacco

    /* FRAGMENT EIGHT */

    private val _wakeUpTime = MutableLiveData<String>()
    val wakeUpTime: LiveData<String> get() = _wakeUpTime

    private val _firstSmoke = MutableLiveData<String>()
    val firstSmoke: LiveData<String> get() = _firstSmoke

    private val _smokingStartTime = MutableLiveData<String>()
    val smokingStartTime: LiveData<String> get() = _smokingStartTime

    /* FRAGMENT NINE */

    private val _isDepressed = MutableLiveData<Boolean>()
    val isDepressed: LiveData<Boolean> get() = _isDepressed

    /* FRAGMENT TEN */

    private val _isSpirit = MutableLiveData<String>()
    val isSpirit: LiveData<String> get() = _isSpirit

    init {
        _currentProgress.value = 10
    }

    /* TOOLBAR ANIMATION */

    fun increaseProgress() {
        _currentProgress.value = _currentProgress.value?.plus(10)
        Log.d("PERSONALIZED_VIEWMODEL", _currentProgress.value.toString())
    }

    fun decreaseProgress() {
        _currentProgress.value = _currentProgress.value?.minus(10)
        Log.d("PERSONALIZED_VIEWMODEL", _currentProgress.value.toString())
    }

    /* FRAGMENT ONE */

    fun setDateOfBirth(date: String) {
        _dateOfBirth.value = date
    }

    fun setProvince(provinceName: String) {
        _province.value = provinceName
    }

    fun setCity(cityName: String) {
        _city.value = cityName
    }

    /* FRAGMENT TWO */

    fun setGender(selectedGender: String) {
        _gender.value = selectedGender
    }

    /* FRAGMENT THREE */

    fun setFirstSmokeDate(date: String) {
        _firstSmokeDate.value = date
    }

    /* FRAGMENT FOUR */

    fun setCigarettesPerDay(total: Int) {
        _cigarettesPerDay.value = total
    }

    fun setCigarettesPerPack(total: Int) {
        _cigarettesPerPack.value = total
    }

    fun setPackPrice(total: Float) {
        _packPrice.value = total
    }

    /* FRAGMENT FIVE */

    fun setIsNicotineMed(value: Boolean) {
        _isNicotineMed.value = value
    }

    /* FRAGMENT SIX */

    fun setIsUsingECigarette(value: Int) {
        _isUsingECigarette.value = value
    }

    /* FRAGMENT SEVEN */

    fun setIsUsingOtherTobacco(value: Int) {
        _isUsingOtherTobacco.value = value
    }

    /* FRAGMENT EIGHT */

    fun setWakeUpTime(time: String) {
        _wakeUpTime.value = time
    }

    fun setFirstSmoke(time: String) {
        _firstSmoke.value = time
    }

    fun setSmokingStartTime(time: String) {
        _smokingStartTime.value = time
    }

    /* FRAGMENT NINE */

    fun setIsDepressed(value: Boolean) {
        _isDepressed.value = value
    }

    /* FRAGMENT TEN */

    fun setIsSpirit(value: String) {
        _isSpirit.value = value
    }
}
