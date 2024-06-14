package com.cpp.unsmoke.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.shop.CreateItemResponse
import com.cpp.unsmoke.data.remote.responses.shop.GetAllMyShopResponse
import com.cpp.unsmoke.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import retrofit2.HttpException

class ShopRepository(
    private var apiService: ApiService,
    private val loginPreferences: LoginPreferences
) {
    fun getMyShop(): LiveData<Result<GetAllMyShopResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllMyShop()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(parseError(e)))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun getMyItems(): LiveData<Result<GetAllMyShopResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllMyItems()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(parseError(e)))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun buyItem(userId: String, itemId: String): LiveData<Result<CreateItemResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.buyItem(userId, itemId)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(parseError(e)))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun getUserId() = loginPreferences.getUserId()

    private fun parseError(e: HttpException): String {
        return try {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val json = JSONObject(errorBody)
                when (e.code()) {
                    400 -> json.getString("message")
                    500 -> json.getString("errors")
                    404 -> json.getString("errors")
                    else -> "Unexpected error: ${e.message()}"
                }
            } else {
                "Unknown error occurred"
            }
        } catch (ex: Exception) {
            "Error parsing response: ${ex.message}"
        }
    }

    companion object {
        @Volatile
        private var instance: ShopRepository? = null

        fun getInstance(
            apiService: ApiService,
            preferences: LoginPreferences
        ): ShopRepository =
            instance ?: synchronized(this) {
                instance ?: ShopRepository(apiService, preferences).also {
                    instance = it
                }
            }
    }
}