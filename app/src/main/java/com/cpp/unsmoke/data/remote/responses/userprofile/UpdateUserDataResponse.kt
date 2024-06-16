package com.cpp.unsmoke.data.remote.responses.userprofile

import com.google.gson.annotations.SerializedName

data class UpdateUserDataResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: DataUpdate? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataUpdate(

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("profile_url")
	val profileUrl: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
