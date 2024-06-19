package com.cpp.unsmoke.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.data.local.preferences.UserPreferences
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.activity.BreathResponse
import com.cpp.unsmoke.data.remote.responses.activity.JournalResponse
import com.cpp.unsmoke.data.remote.responses.activity.RelapseResponse
import com.cpp.unsmoke.data.remote.retrofit.ApiService
import com.cpp.unsmoke.utils.helper.date.Date
import com.cpp.unsmoke.utils.helper.gamification.Gamification
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import retrofit2.HttpException

class ActivityRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences,
    private val loginPreferences: LoginPreferences
) {

    fun sendBreathData(duration: Int): LiveData<Result<BreathResponse>> = liveData {
        emit(Result.Loading)
        try {
            val accessToken = runBlocking {
                loginPreferences.getToken().first()
            }

            val userId = runBlocking {
                loginPreferences.getUserId().first()
            }

            val response = apiService.sendBreathActivity(
                "Bearer $accessToken",
                userId.toString(),
                duration,
                Gamification.BREATH_REWARD,
                Date.getEpochTimeNow()
            )
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(parseError(e)))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun sendJournalData(content: String): LiveData<Result<JournalResponse>> = liveData {
        emit(Result.Loading)
        try {
            val accessToken = runBlocking {
                loginPreferences.getToken().first()
            }

            val userId = runBlocking {
                loginPreferences.getUserId().first()
            }

            val epoch = Date.getEpochTimeNow()

            val response = apiService.sendJournalActivity(
                "Bearer $accessToken",
                userId.toString(),
                "journal_${userId}_${Date.getDateFromEpoch(epoch)}",
                content,
                Gamification.JOURNAL_REWARD,
                epoch
            )
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(parseError(e)))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun sendRelapseData(cigarettesThisDay: Int): LiveData<Result<RelapseResponse>> = liveData {
        emit(Result.Loading)
        try {
            val accessToken = runBlocking {
                loginPreferences.getToken().first()
            }

            val userId = runBlocking {
                loginPreferences.getUserId().first()
            }

            val response = apiService.sendRelapseActivity(
                "Bearer $accessToken",
                userId.toString(),
                Gamification.RELAPSE_REWARD,
                cigarettesThisDay,
                Date.getEpochTimeNow()
            )
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
        private var instance: ActivityRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences,
            loginPreferences: LoginPreferences
        ): ActivityRepository =
            instance ?: synchronized(this) {
                instance ?: ActivityRepository(apiService, userPreferences, loginPreferences).also {
                    instance = it
                }
            }
    }
}