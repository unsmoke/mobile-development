package com.cpp.unsmoke.data.remote.responses.personalized

import com.google.gson.annotations.SerializedName

data class GetPersonalizedResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("cigarettes_per_pack")
	val cigarettesPerPack: Int? = null,

	@field:SerializedName("last_7_days")
	val last7Days: Boolean? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("date_of_birth")
	val dateOfBirth: String? = null,

	@field:SerializedName("is_spirit")
	val isSpirit: Boolean? = null,

	@field:SerializedName("motivation")
	val motivation: String? = null,

	@field:SerializedName("is_e_cigarette")
	val isECigarette: Int? = null,

	@field:SerializedName("is_nicotine_med")
	val isNicotineMed: Boolean? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("is_other_tobacco")
	val isOtherTobacco: Int? = null,

	@field:SerializedName("cigarettes_per_day")
	val cigarettesPerDay: Int? = null,

	@field:SerializedName("first_cigarette_date")
	val firstCigaretteDate: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("smoking_start_time")
	val smokingStartTime: Int? = null,

	@field:SerializedName("pack_price")
	val packPrice: Any? = null,

	@field:SerializedName("is_depressed")
	val isDepressed: Boolean? = null
)
