package com.cpp.unsmoke.data.remote.retrofit

import com.cpp.unsmoke.data.remote.responses.auth.LoginResponse
import com.cpp.unsmoke.data.remote.responses.auth.RefreshResponse
import com.cpp.unsmoke.data.remote.responses.auth.RegisterResponse
import com.cpp.unsmoke.data.remote.responses.leaderboard.LeaderboardResponse
import com.cpp.unsmoke.data.remote.responses.personalized.CityResponse
import com.cpp.unsmoke.data.remote.responses.personalized.CreatePersonalizedResponse
import com.cpp.unsmoke.data.remote.responses.personalized.GetPersonalizedResponse
import com.cpp.unsmoke.data.remote.responses.personalized.ProvinceResponse
import com.cpp.unsmoke.data.remote.responses.shop.CreateItemResponse
import com.cpp.unsmoke.data.remote.responses.shop.GetAllMyShopResponse
import com.cpp.unsmoke.data.remote.responses.userplan.GetActiveUserPlanResponse
import com.cpp.unsmoke.data.remote.responses.userplan.GetAllUserPlanResponse
import com.cpp.unsmoke.data.remote.responses.userprofile.UpdateUserDataResponse
import com.cpp.unsmoke.data.remote.responses.userprofile.UserDetailDataResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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
        @Field("refreshToken") refreshToken: String
    ): RefreshResponse

    @FormUrlEncoded
    @POST("health")
    suspend fun createPersonalized(
        @Header("Authorization") authHeader: String,
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
        @Field("city") city: String,
        @Field("last_7_days") last7Days: Boolean,
        @Field("motivation") motivation: String,
    ): CreatePersonalizedResponse

    @GET("health")
    suspend fun getPersonalizedPlan(
        @Header("Authorization") authHeader: String,
    ): GetPersonalizedResponse

    @GET("location/province")
    suspend fun getProvince(): ProvinceResponse

    @GET("location/city/{provinceId}")
    suspend fun getCity(
        @Path("provinceId") provinceId: Int
    ): CityResponse

    @GET("user-plan/all")
    suspend fun getAllUserPlan(
        @Header("Authorization") authHeader: String,
    ) : GetAllUserPlanResponse

    @GET("user-plan")
    suspend fun getActiveUserPlan(
        @Header("Authorization") authHeader: String,
    ) : GetActiveUserPlanResponse

    @FormUrlEncoded
    @PUT("user-plan")
    suspend fun updateUserPlan(
        @Header("Authorization") authHeader: String,
        @Field("plan_id") planId: Int
    ): GetActiveUserPlanResponse

    @GET("shop")
    suspend fun getAllMyShop(
        @Header("Authorization") authHeader: String,
    ): GetAllMyShopResponse

    @GET("inventory")
    suspend fun getAllMyItems(
        @Header("Authorization") authHeader: String,
    ): GetAllMyShopResponse

    @FormUrlEncoded
    @POST("inventory")
    suspend fun buyItem(
        @Header("Authorization") authHeader: String,
        @Field("user_id") userId: String,
        @Field("item_id") itemId: String
    ): CreateItemResponse

    @GET("leaderboard/province/{provinceId}")
    suspend fun getLeaderboardByProvince(
        @Header("Authorization") authHeader: String,
        @Path("provinceId") provinceId: Int,
        @Query("page") page: Int = 1,
        @Query("per_page") size: Int = 5
    ): LeaderboardResponse

    @GET("user")
    suspend fun getUserDetail(
        @Header("Authorization") authHeader: String,
    ): UserDetailDataResponse

    @Multipart
    @PUT("profile")
    suspend fun updateUserProfile(
        @Header("Authorization") authHeader: String,
        @Part file: MultipartBody.Part,
        @Part("username") fullName: String,
    ): UpdateUserDataResponse
}