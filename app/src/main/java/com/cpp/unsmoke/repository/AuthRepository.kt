package com.cpp.unsmoke.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.data.remote.responses.auth.LoginResponse
import com.cpp.unsmoke.data.remote.retrofit.ApiService
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.auth.RefreshResponse
import com.cpp.unsmoke.data.remote.responses.auth.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException

class AuthRepository(
    private var apiService: ApiService,
    private val loginPreferences: LoginPreferences
) {

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            if (response.code == 200) { // Assuming 200 is the success code
                response.data?.let { loginData ->
                    saveToken(loginData.accessToken ?: "", loginData.refreshToken ?: "")
                }
            }
            emit(Result.Success(response))
            Log.d("LOGIN", "data success : ${response.data?.email.toString()}")
        } catch (e: HttpException) {
            val errorResponse = parseError(e)
            Log.e("LOGIN", errorResponse)
            emit(Result.Error(errorResponse))
        } catch (e: Exception) {
            Log.e("LOGIN", e.message.toString())
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun register(fullName: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(fullName, email, password)
            emit(Result.Success(response))
        } catch (e: HttpException){
            val errorResponse = parseError(e)
            Log.e("REGISTER", errorResponse)
            emit(Result.Error(errorResponse))
        } catch (e: Exception) {
            Log.e("REGISTER", e.message.toString())
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun refresh(): LiveData<Result<RefreshResponse>> = liveData {
        emit(Result.Loading)
        try {
            val refreshToken = runBlocking { getRefreshToken().first() }
            val response = apiService.refresh(refreshToken.toString())
            if (refreshToken.isNullOrEmpty()) {
                emit(Result.Error("Refresh token is missing"))
                return@liveData
            }
            if (response.code == 200) { // Assuming 200 is the success code
                response.data?.let { refreshData ->
                    updateToken(refreshData.accessToken ?: "")
                }
                emit(Result.Success(response))
            } else{
                emit(Result.Error("Failed to refresh token: ${response.code}"))
            }
        } catch (e: HttpException) {
            val errorResponse = parseError(e)
            Log.e("REFRESH_TOKEN", errorResponse)
            emit(Result.Error(errorResponse))
        } catch (e: Exception) {
            Log.e("REFRESH_TOKEN", e.message.toString())
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    private suspend fun saveToken(token: String, refreshToken: String) = loginPreferences.saveToken(token, refreshToken)

    suspend fun loginPref() = loginPreferences.loginPref()

    fun getLoginStatus() = loginPreferences.getLoginStatus()

    private fun getRefreshToken() = loginPreferences.getRefreshToken()

    private suspend fun updateToken(token: String) = loginPreferences.updateToken(token)

    private fun parseError(e: HttpException): String {
        return try {
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                val json = JSONObject(errorBody)
                when (e.code()) {
                    400 -> {
                        val message = json.getString("message")
                        if (message.contains("email")) {
                            return "Email is required"
                        } else if (message.contains("password")) {
                            return "Password is required"
                        } else if (message.contains("fullName")){
                            return "Full Name is required"
                        }
                        return message
                    }
                    500 -> {
                        return json.getString("errors")
                    }
                    else -> {
                        return "Unexpected error: ${e.message()}"
                    }
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
        private var instance: AuthRepository? = null

        fun getInstance(
            apiService: ApiService,
            preferences: LoginPreferences
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService, preferences).also {
                    instance = it
                }
            }
    }

}