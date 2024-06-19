package com.cpp.unsmoke.data.remote.responses.auth

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: DataForgotPw? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataForgotPw(

	@field:SerializedName("message")
	val message: String? = null
)
