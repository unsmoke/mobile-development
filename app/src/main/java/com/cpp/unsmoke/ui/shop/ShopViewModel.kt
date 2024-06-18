package com.cpp.unsmoke.ui.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.shop.CreateItemResponse
import com.cpp.unsmoke.data.remote.responses.shop.EquipItemResponse
import com.cpp.unsmoke.data.remote.responses.shop.GetAllMyShopResponse
import com.cpp.unsmoke.repository.SettingRepository
import com.cpp.unsmoke.repository.ShopRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ShopViewModel(private val shopRepository: ShopRepository, private val settingRepository: SettingRepository): ViewModel() {

    private val _currentLungUrl = MutableLiveData<String>()
    val currentLungUrl: LiveData<String> get() = _currentLungUrl

    private val _currentLungId = MutableLiveData<String>()
    val currentLungId: LiveData<String> get() = _currentLungId

    fun getMyShop(): LiveData<Result<GetAllMyShopResponse>> {
        return shopRepository.getMyShop()
    }

    fun getMyInventory(): LiveData<Result<GetAllMyShopResponse>> {
        return shopRepository.getMyItems()
    }

    fun buyItem(userId: String, itemId: String): LiveData<Result<CreateItemResponse>> {
        return shopRepository.buyItem(userId, itemId)
    }

    fun equipItem(userId: String, itemId: String): LiveData<Result<EquipItemResponse>> {
        return shopRepository.equipItem(userId, itemId)
    }

    fun setLungUrl() {
        viewModelScope.launch {
            _currentLungUrl.value = shopRepository.getLungUrl().first()
        }
    }

    fun setLungId() {
        viewModelScope.launch {
            _currentLungId.value = shopRepository.getLungId().first()
        }
    }

    fun setLungUrlToLcal(url: String){
        runBlocking {
            shopRepository.setLungUrl(url)
        }
    }

    fun setLungIdToLocal(id: String) {
        runBlocking {
            shopRepository.setLungId(id)
        }
    }

    fun logout() {
        runBlocking {
            settingRepository.logout()
        }
    }

    fun getUserId() = runBlocking {
        shopRepository.getUserId().first()
    }
}