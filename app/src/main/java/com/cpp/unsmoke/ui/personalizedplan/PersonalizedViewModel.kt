package com.cpp.unsmoke.ui.personalizedplan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.personalized.CityResponse
import com.cpp.unsmoke.data.remote.responses.personalized.CreatePersonalizedResponse
import com.cpp.unsmoke.data.remote.responses.personalized.DataItemCity
import com.cpp.unsmoke.data.remote.responses.personalized.ProvinceResponse
import com.cpp.unsmoke.data.remote.responses.userplan.GetActiveUserPlanResponse
import com.cpp.unsmoke.data.remote.responses.userplan.GetAllUserPlanResponse
import com.cpp.unsmoke.repository.PersonalizedPlanRepository

class PersonalizedViewModel(private val personalizedPlanRepository: PersonalizedPlanRepository): ViewModel() {
    /* TOOLBAR ANIMATION */

    private val _currentProgress = MutableLiveData<Int>()
    val currentProgress: LiveData<Int> get() = _currentProgress

    /* FRAGMENT ONE */

    private val _dateOfBirth = MutableLiveData<String>()
    val dateOfBirth: LiveData<String> get() = _dateOfBirth

    private val _province = MutableLiveData<Int>()
    val province: LiveData<Int> get() = _province

    private val _city = MutableLiveData<Int>()
    val city: LiveData<Int> get() = _city

    private val _cities = MutableLiveData<List<DataItemCity?>?>()
    val cities: LiveData<List<DataItemCity?>?> get() = _cities

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

    private val _smokingStartTime = MutableLiveData<Int>()
    val smokingStartTime: LiveData<Int> get() = _smokingStartTime

    /* FRAGMENT NINE */

    private val _isDepressed = MutableLiveData<Boolean>()
    val isDepressed: LiveData<Boolean> get() = _isDepressed

    /* FRAGMENT TEN */

    private val _isSpirit = MutableLiveData<Boolean>()
    val isSpirit: LiveData<Boolean> get() = _isSpirit

    /* FRAGMENT ELEVEN */

    private val _isLast7Days = MutableLiveData<Boolean>()
    val isLast7Days: LiveData<Boolean> get() = _isLast7Days

    /* FRAGMENT TWELVE */

    private val _motivation = MutableLiveData<String>()
    val motivation: LiveData<String> get() = _motivation

    private val _provincesLiveData = MutableLiveData<Result<ProvinceResponse>>()
    val provincesLiveData: LiveData<Result<ProvinceResponse>> get() = _provincesLiveData

    private val _citiesLiveData = MutableLiveData<Result<CityResponse>>()
    val citiesLiveData: LiveData<Result<CityResponse>> get() = _citiesLiveData

    init {
        _currentProgress.value = 8
    }

    /* TOOLBAR ANIMATION */

    fun increaseProgress() {
        _currentProgress.value = _currentProgress.value?.plus(8)
        Log.d("PERSONALIZED_VIEWMODEL", _currentProgress.value.toString())
    }

    fun decreaseProgress() {
        _currentProgress.value = _currentProgress.value?.minus(8)
        Log.d("PERSONALIZED_VIEWMODEL", _currentProgress.value.toString())
    }

    /* FRAGMENT ONE */

    fun setDateOfBirth(date: String) {
        _dateOfBirth.value = date
    }

    fun setProvince(idProvince: Int) {
        _province.value = idProvince
    }

    fun setCity(idCity: Int) {
        _city.value = idCity
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

    fun setSmokingStartTime(time: Int) {
        _smokingStartTime.value = time
    }

    /* FRAGMENT NINE */

    fun setIsDepressed(value: Boolean) {
        _isDepressed.value = value
    }

    /* FRAGMENT TEN */

    fun setIsSpirit(value: Boolean) {
        _isSpirit.value = value
    }

    /* FRAGMENT ELEVEN */

    fun setIsLast7Days(value: Boolean) {
        _isLast7Days.value = value
    }

    /* FRAGMENT TWELVE */

    fun setMotivation(value: String) {
        _motivation.value = value
    }

    /* GET ALL DATA */
    fun getAllData() {
        val allData = """
            Date of Birth: ${_dateOfBirth.value}
            Province: ${_province.value}
            City: ${_city.value}
            Gender: ${_gender.value}
            First Smoke Date: ${_firstSmokeDate.value}
            Cigarettes Per Day: ${_cigarettesPerDay.value}
            Cigarettes Per Pack: ${_cigarettesPerPack.value}
            Pack Price: ${_packPrice.value}
            Is Using Nicotine Medication: ${_isNicotineMed.value}
            Is Using E-Cigarette: ${_isUsingECigarette.value}
            Is Using Other Tobacco: ${_isUsingOtherTobacco.value}
            Wake Up Time: ${_wakeUpTime.value}
            First Smoke Time: ${_firstSmoke.value}
            Smoking Start Time: ${_smokingStartTime.value}
            Is Depressed: ${_isDepressed.value}
            Spirit: ${_isSpirit.value}
            Province: ${_province.value.toString()}
            City: ${_city.value.toString()}
            Last 7 Days: ${_isLast7Days.value}
            Motivation: ${_motivation.value}
        """.trimIndent()

        Log.d("PERSONALIZED_VIEWMODEL", allData)
    }

    /* DROPDOWN */

    fun loadProvinces() {
        personalizedPlanRepository.getProvince().observeForever {
            _provincesLiveData.postValue(it)
        }
    }

    fun loadCities(provinceId: Int) {
        personalizedPlanRepository.getCities(provinceId).observeForever {
            _citiesLiveData.postValue(it)
        }
    }

    fun setCities(cities: List<DataItemCity?>?){
        _cities.value = cities
    }

    /* SET PERSONALIZED */

    fun setPersonalizedPlan(): LiveData<Result<CreatePersonalizedResponse>> {
        val dateOfBirth = _dateOfBirth.value.toString() ?: ""
        val gender = _gender.value.toString() ?: ""
        val smokingStartTime = _smokingStartTime.value ?: 0
        val isNicotineMed = _isNicotineMed.value ?: false
        val isUsingECigarette = _isUsingECigarette.value ?: 0
        val firstSmokeDate = _firstSmokeDate.value.toString() ?: ""
        val isDepressed = _isDepressed.value ?: false
        val isUsingOtherTobacco = _isUsingOtherTobacco.value ?: 4
        val isSpirit = _isSpirit.value ?: false
        val cigarettesPerDay = _cigarettesPerDay.value ?: 0
        val cigarettesPerPack = _cigarettesPerPack.value ?: 0
        val packPrice = _packPrice.value ?: 0.0f
        val province = _province.value.toString() ?: ""
        val city = _city.value.toString() ?: ""
        val isLast7Days = _isLast7Days.value ?: false
        val motivation = _motivation.value.toString() ?: ""

        return personalizedPlanRepository.setPersonalizedPlan(
            dateOfBirth,
            gender,
            smokingStartTime,
            isNicotineMed,
            isUsingECigarette,
            firstSmokeDate,
            isDepressed,
            isUsingOtherTobacco,
            isSpirit,
            cigarettesPerDay,
            cigarettesPerPack,
            packPrice,
            province,
            city,
            isLast7Days,
            motivation
        )
    }

    /* GET ALL USER PLAN */

    fun getUserPlan(): LiveData<Result<GetAllUserPlanResponse>> {
        return personalizedPlanRepository.getAllUserPlan()
    }

    /* UPDATE USER PLAN */

    fun updateUserPlan(planId: Int): LiveData<Result<GetActiveUserPlanResponse>> {
        return personalizedPlanRepository.updateUserPlan(planId)
    }

}
