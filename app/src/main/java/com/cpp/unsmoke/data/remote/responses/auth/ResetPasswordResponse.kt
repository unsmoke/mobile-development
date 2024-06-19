package com.cpp.unsmoke.data.remote.responses.auth

import com.google.gson.annotations.SerializedName

data class ResetPasswordResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: DataRestPw? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataRestPw(

	@field:SerializedName("message")
	val message: String? = null
)
