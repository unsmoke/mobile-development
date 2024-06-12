package com.cpp.unsmoke.data.remote.responses.userplan

import com.google.gson.annotations.SerializedName

data class GetAllUserPlanResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItem(

	@field:SerializedName("duration")
	val duration: Int? = null,

	@field:SerializedName("original_cigarettes_quota")
	val originalCigarettesQuota: List<Int?>? = null,

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("probability")
	val probability: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("plan_id")
	val planId: Int? = null
)
