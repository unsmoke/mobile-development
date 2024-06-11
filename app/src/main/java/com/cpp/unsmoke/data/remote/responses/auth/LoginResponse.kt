package com.cpp.unsmoke.data.remote.responses.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("code")
	val code: Int? = null,

    @field:SerializedName("data")
	val data: LoginData? = null,

    @field:SerializedName("status")
	val status: String? = null
)

data class LoginData(

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("accessToken")
	val accessToken: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("refreshToken")
	val refreshToken: String? = null
)
