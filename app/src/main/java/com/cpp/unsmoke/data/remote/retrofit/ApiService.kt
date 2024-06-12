package com.cpp.unsmoke.data.remote.retrofit

import com.cpp.unsmoke.data.remote.responses.auth.LoginResponse
import com.cpp.unsmoke.data.remote.responses.auth.RefreshResponse
import com.cpp.unsmoke.data.remote.responses.auth.RegisterResponse
import com.cpp.unsmoke.data.remote.responses.personalized.CityResponse
import com.cpp.unsmoke.data.remote.responses.personalized.CreatePersonalizedResponse
import com.cpp.unsmoke.data.remote.responses.personalized.ProvinceResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @FormUrlEncoded
    @POST("health")
    suspend fun createPersonalized(
        @Field("date_of_birth") dateOfBirth: String,
        @Field("gender") gender: String,
        @Field("smoking_start_time") smokingStartTime: Int,
        @Field("is_nicotine_med") isNicotineMed: Boolean,
        @Field("is_e_cigarette") isECigarette: Int,
        @Field("first_cigarette_date") firstCigaretteDate: String,
        @Field("is_depressed") isDepressed: Boolean,
        @Field("is_other_tobacco") isOtherTobacco: Int,
        @Field("is_spirit") isSpirit: Boolean,
        @Field("cigarettes_per_day") cigarettesPerDay: Int,
        @Field("cigarettes_per_pack") cigarettesPerPack: Int,
        @Field("pack_price") packPrice: Float,
        @Field("province") province: String,
        @Field("city") city: String
    ): CreatePersonalizedResponse

    @GET("health")
    suspend fun getPersonalizedPlan(): CreatePersonalizedResponse

    @GET("location/province")
    suspend fun getProvince(): ProvinceResponse

    @GET("location/city/{provinceId}")
    suspend fun getCity(
        @Path("provinceId") provinceId: Int
    ): CityResponse
}