package com.cpp.unsmoke.data.remote.retrofit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.auth.RefreshResponse
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class TokenAuthenticator(
    private val loginPreferences: LoginPreferences
) : Authenticator {

    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401) {
            val refreshToken = runBlocking { loginPreferences.getRefreshToken().first() }
            val currentAccessToken = runBlocking { loginPreferences.getToken().first() }

            return if (!refreshToken.isNullOrEmpty() && !currentAccessToken.isNullOrEmpty()) {
                val newAccessToken = runBlocking {
                    when (val result = refreshToken(refreshToken, currentAccessToken)) {
                        is Result.Success -> result.data.data?.accessToken
                        else -> null
                    }
                }

                Log.d("TokenAuthenticator", "New Access Token: $newAccessToken")

                newAccessToken?.let {
                    runBlocking {
                        loginPreferences.updateToken(it)
                    }
                    response.request.newBuilder()
                        .header("Authorization", "Bearer $it")
                        .build()
                }
            } else {
                Log.d("TokenAuthenticator", "Refresh token is null, logging out user")
                logoutUser()
                null
            }
        } else {
            Log.d("TokenAuthenticator", "Authentication response code: ${response.code}")
            return null
        }
    }

    private suspend fun refreshToken(refreshToken: String, accessToken: String): Result<RefreshResponse> = runBlocking {
        try {
            val apiService = ApiConfig.getApiServiceWithoutAuth()
            val authHeader = "Bearer $accessToken"
            val response = apiService.refresh(authHeader, refreshToken)
            Result.Success(response)
        } catch (e: HttpException) {
            val errorResponse = parseError(e)
            Log.e("REFRESH_TOKEN", errorResponse)
            Result.Error(errorResponse)
        } catch (e: Exception) {
            Log.e("REFRESH_TOKEN", e.message.toString())
            Result.Error(e.message ?: "An unknown error occurred")
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
                    else -> "Unexpected error: ${e.message()}"
                }
            } else {
                "Unknown error occurred"
            }
        } catch (ex: Exception) {
            "Error parsing response: ${ex.message}"
        }
    }

    private fun logoutUser() {
        runBlocking {
            loginPreferences.logout()
        }
    }
}

