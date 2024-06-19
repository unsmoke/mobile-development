package com.cpp.unsmoke.data.remote.responses.userprofile

import com.google.gson.annotations.SerializedName

data class UserDetailDataResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: DataUserDetail? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataUserDetail(

	@field:SerializedName("current_lung")
	val currentLung: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("profile_url")
	val profileUrl: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("balance_coin")
	val balanceCoin: Int? = null,

	@field:SerializedName("time_zone")
	val timeZone: Any? = null,

	@field:SerializedName("cigarettes_quota")
	val cigarettesQuota: List<Int?>? = null,

	@field:SerializedName("current_day")
	val currentDay: Int? = null,

	@field:SerializedName("money_saved")
	val moneySaved: Double? = null,

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("is_premium")
	val isPremium: Boolean? = null,

	@field:SerializedName("province")
	val province: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("rank")
	val rank: Int? = null,

	@field:SerializedName("cigarettes_avoided")
	val cigarettesAvoided: Int? = null,

	@field:SerializedName("exp")
	val exp: Int? = null,

	@field:SerializedName("streak_count")
	val streakCount: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
