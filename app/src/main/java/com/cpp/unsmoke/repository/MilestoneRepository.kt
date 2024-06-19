package com.cpp.unsmoke.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.data.remote.responses.milestone.MilestoneResponse
import com.cpp.unsmoke.data.remote.retrofit.ApiService
import com.cpp.unsmoke.data.remote.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import retrofit2.HttpException

class MilestoneRepository(
    private val apiService: ApiService,
    private val loginPreferences: LoginPreferences,
) {

    fun getAllMilestones(): LiveData<Result<MilestoneResponse>> = liveData {
        emit(Result.Loading)
        try {
            val accessToken = runBlocking {
                loginPreferences.getToken().first()
            }

            val response = apiService.getAllMilestones("Bearer $accessToken")
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(parseError(e)))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun getMilestoneById(): LiveData<Result<MilestoneResponse>> = liveData {
        emit(Result.Loading)
        try {
            val accessToken = runBlocking {
                loginPreferences.getToken().first()
            }

            val userId = runBlocking {
                loginPreferences.getUserId().first()
            }

            val response = apiService.getMilestonesById("Bearer $accessToken", userId.toString())
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(parseError(e)))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
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
        private var instance: MilestoneRepository? = null

        fun getInstance(
            apiService: ApiService,
            preferences: LoginPreferences
        ): MilestoneRepository =
            instance ?: synchronized(this) {
                instance ?: MilestoneRepository(apiService, preferences).also {
                    instance = it
                }
            }
    }
}