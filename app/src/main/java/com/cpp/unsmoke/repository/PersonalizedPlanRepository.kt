package com.cpp.unsmoke.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.data.local.preferences.UserPreferences
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.personalized.CityResponse
import com.cpp.unsmoke.data.remote.responses.personalized.CreatePersonalizedResponse
import com.cpp.unsmoke.data.remote.responses.personalized.GetPersonalizedResponse
import com.cpp.unsmoke.data.remote.responses.personalized.ProvinceResponse
import com.cpp.unsmoke.data.remote.responses.userplan.GetActiveUserPlanResponse
import com.cpp.unsmoke.data.remote.responses.userplan.GetAllUserPlanResponse
import com.cpp.unsmoke.data.remote.retrofit.ApiConfig
import com.cpp.unsmoke.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import retrofit2.HttpException

class PersonalizedPlanRepository(
    private var apiService: ApiService,
    private val loginPreferences: LoginPreferences,
    private val userPreferences: UserPreferences
) {

    fun setPersonalizedPlan(
        dateOfBirth: String,
        gender: String,
        smokingStartTime: Int,
        isNicotineMed: Boolean,
        isECigarette: Int,
        firstCigaretteDate: String,
        isDepressed: Boolean,
        isOtherTobacco: Int,
        isSpirit: Boolean,
        cigarettesPerDay: Int,
        cigarettesPerPack: Int,
        packPrice: Float,
        province: String,
        city: String,
        last7Days: Boolean,
        motivation: String
    ): LiveData<Result<CreatePersonalizedResponse>> = liveData {
        emit(Result.Loading)
        try {
            val accessToken = runBlocking {
                loginPreferences.getToken().first()
            }

            Log.d("PersonalizedPlanRepository", "setPersonalizedPlan: $accessToken)")

            val response = apiService.createPersonalized(
                "Bearer $accessToken",
                dateOfBirth,
                gender,
                smokingStartTime,
                isNicotineMed,
                isECigarette,
                firstCigaretteDate,
                isDepressed,
                isOtherTobacco,
                isSpirit,
                cigarettesPerDay,
                cigarettesPerPack,
                packPrice,
                province,
                city,
                last7Days,
                motivation
            )
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorResponse = parseError(e)
            Log.e("SET_PERSONALIZED_PLAN", errorResponse)
            emit(Result.Error(errorResponse))
        } catch (e: Exception) {
            Log.e("SET_PERSONALIZED_PLAN", e.message.toString())
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun getPersonalizedPlan(): LiveData<Result<GetPersonalizedResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val accessToken = runBlocking {
                    loginPreferences.getToken().first()
                }

                Log.d("PersonalizedPlanRepository", "setPersonalizedPlan: $accessToken)")

                val response = apiService.getPersonalizedPlan("Bearer $accessToken")
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val token = runBlocking {
                    loginPreferences.getToken().first()
                }

                Log.d("PersonalizedPlanRepository", "getPersonalizedPlan: token: $token")
                val errorResponse = parseError(e)
                Log.e("GET_PERSONALIZED_PLAN", errorResponse)
                emit(Result.Error(errorResponse))
            } catch (e: Exception) {
                Log.e("GET_PERSONALIZED_PLAN", e.message.toString())
                emit(Result.Error(e.message ?: "An unknown error occurred"))
            }
        }

    fun getProvince(): LiveData<Result<ProvinceResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getProvince()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorResponse = parseError(e)
            Log.e("GET_PROVINCE", errorResponse)
            emit(Result.Error(errorResponse))
        } catch (e: Exception) {
            Log.e("GET_PROVINCE", e.message.toString())
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun getCities(provinceId: Int): LiveData<Result<CityResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getCity(provinceId)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorResponse = parseError(e)
            Log.e("GET_CITIES", errorResponse)
            emit(Result.Error(errorResponse))
        } catch (e: Exception) {
            Log.e("GET_CITIES", e.message.toString())
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun getAllUserPlan(): LiveData<Result<GetAllUserPlanResponse>> = liveData {
        emit(Result.Loading)
        try {
            val accessToken = runBlocking {
                loginPreferences.getToken().first()
            }

            Log.d("PersonalizedPlanRepository", "setPersonalizedPlan: $accessToken)")

            val response = apiService.getAllUserPlan("Bearer $accessToken")
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorResponse = parseError(e)
            Log.e("GET_ALL_USER_PLAN", errorResponse)
            emit(Result.Error(errorResponse))
        } catch (e: Exception) {
            Log.e("GET_ALL_USER_PLAN", e.message.toString())
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun updateUserPlan(idPlan: Int): LiveData<Result<GetActiveUserPlanResponse>> = liveData {
        emit(Result.Loading)
        try {
            val accessToken = runBlocking {
                loginPreferences.getToken().first()
            }

            Log.d("PersonalizedPlanRepository", "setPersonalizedPlan: $accessToken)")

            val response = apiService.updateUserPlan("Bearer $accessToken", idPlan)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorResponse = parseError(e)
            Log.e("UPDATE_USER_PLAN", errorResponse)
            emit(Result.Error(errorResponse))
        } catch (e: Exception) {
            Log.e("UPDATE_USER_PLAN", e.message.toString())
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    suspend fun setProv(provId: String) = runBlocking {
        userPreferences.setProv(provId)
    }

    suspend fun setCity(cityId: String) = runBlocking {
        userPreferences.setCity(cityId)
    }

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
        private var instance: PersonalizedPlanRepository? = null

        fun getInstance(
            apiService: ApiService,
            loginPreferences: LoginPreferences,
            userPreferences: UserPreferences
        ): PersonalizedPlanRepository =
            instance ?: synchronized(this) {
                instance ?: PersonalizedPlanRepository(apiService, loginPreferences, userPreferences).also {
                    instance = it
                }
            }
    }
}