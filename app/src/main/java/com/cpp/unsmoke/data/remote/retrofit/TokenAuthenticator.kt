package com.cpp.unsmoke.data.remote.retrofit

import android.util.Log
import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import com.cpp.unsmoke.data.remote.Result

class TokenAuthenticator(
    private val authRepository: AuthRepository,
    private val loginPreferences: LoginPreferences
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking { loginPreferences.getRefreshToken().first() }

        return if (refreshToken != null) {
            val newAccessToken = runBlocking {
                when (val result = authRepository.refresh().value) {
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
            null
        }
    }
}
