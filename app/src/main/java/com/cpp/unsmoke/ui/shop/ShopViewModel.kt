package com.cpp.unsmoke.ui.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.shop.CreateItemResponse
import com.cpp.unsmoke.data.remote.responses.shop.GetAllMyShopResponse
import com.cpp.unsmoke.repository.SettingRepository
import com.cpp.unsmoke.repository.ShopRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ShopViewModel(private val shopRepository: ShopRepository, private val settingRepository: SettingRepository): ViewModel() {
    fun getMyShop(): LiveData<Result<GetAllMyShopResponse>> {
        return shopRepository.getMyShop()
    }

    fun getMyInventory(): LiveData<Result<GetAllMyShopResponse>> {
        return shopRepository.getMyItems()
    }

    fun buyItem(userId: String, itemId: String): LiveData<Result<CreateItemResponse>> {
        return shopRepository.buyItem(userId, itemId)
    }

    fun logout() {
        viewModelScope.launch {
            settingRepository.logout()
        }
    }

    fun getUserId() = runBlocking {
        shopRepository.getUserId().first()
    }
}