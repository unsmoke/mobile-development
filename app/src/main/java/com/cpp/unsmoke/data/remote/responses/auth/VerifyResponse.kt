package com.cpp.unsmoke.data.remote.responses.auth

import com.google.gson.annotations.SerializedName

data class VerifyResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)
