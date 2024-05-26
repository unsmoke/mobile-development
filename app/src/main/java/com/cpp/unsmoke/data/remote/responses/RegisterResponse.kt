package com.cpp.unsmoke.data.remote.responses

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: RegisterData? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class RegisterData(

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
