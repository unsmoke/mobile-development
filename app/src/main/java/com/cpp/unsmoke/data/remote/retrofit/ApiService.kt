package com.cpp.unsmoke.data.remote.retrofit

import com.cpp.unsmoke.data.remote.responses.LoginResponse
import com.cpp.unsmoke.data.remote.responses.RefreshResponse
import com.cpp.unsmoke.data.remote.responses.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("fullName") fullName: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("refresh")
    suspend fun refresh(
        @Field("refreshToken") refreshToken: String,
    ): RefreshResponse
}