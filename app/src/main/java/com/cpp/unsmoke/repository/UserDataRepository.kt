package com.cpp.unsmoke.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.data.local.preferences.UserPreferences
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.userprofile.UpdateUserDataResponse
import com.cpp.unsmoke.data.remote.responses.userprofile.UserDetailDataResponse
import com.cpp.unsmoke.data.remote.retrofit.ApiConfig
import com.cpp.unsmoke.data.remote.retrofit.ApiService
import com.cpp.unsmoke.utils.helper.ui.reduceFileImage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File

class UserDataRepository(
    private var apiService: ApiService,
    private val preferences: LoginPreferences,
    private val userPreferences: UserPreferences
) {

    fun getUserData(): LiveData<Result<UserDetailDataResponse>> = liveData {
        Result.Loading
        try {
            val token = runBlocking {
                preferences.getToken().first()
            }
            val response = apiService.getUserDetail("Bearer $token")
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val error = parseError(e)
            emit(Result.Error(error))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error occurred"))
        }
    }

    fun updateDataProfile(
        username: String,
        file: File?
    ): LiveData<Result<UpdateUserDataResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = runBlocking {
                preferences.getToken().first()
            }

            if (file != null) {
                val newUsername = username.toRequestBody("text/plain".toMediaType())
                val fileReduced = file.reduceFileImage()
                val requestFile = fileReduced.asRequestBody("image/jpeg".toMediaType())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                val response = apiService.updateUserProfile("Bearer $token", newUsername, body)
                Log.d("UserDataRepository", "updateDataProfile: $response")
                emit(Result.Success(response))
            } else {
                emit(Result.Error("File is null"))
            }
        } catch (e: HttpException) {
            val error = parseError(e)
            Log.e("UserDataRepository", "updateDataProfile: $error")
            emit(Result.Error(error))
        } catch (e: Exception) {
            Log.e("UserDataRepository", "updateDataProfile: ${e.message}")
            emit(Result.Error(e.message ?: "Unknown error occurred"))
        }
    }

    fun getLungUrl() = userPreferences.getUserLung()

    suspend fun saveLungUrl(url: String) {
        userPreferences.setUserLung(url)
    }

    fun getLungId() = userPreferences.getUserLungId()

    fun getUserId() = preferences.getUserId()

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
        private var instance: UserDataRepository? = null

        fun getInstance(
            apiService: ApiService,
            preferences: LoginPreferences,
            userPreferences: UserPreferences
        ): UserDataRepository =
            instance ?: synchronized(this) {
                instance ?: UserDataRepository(apiService, preferences, userPreferences).also {
                    instance = it
                }
            }
    }
}