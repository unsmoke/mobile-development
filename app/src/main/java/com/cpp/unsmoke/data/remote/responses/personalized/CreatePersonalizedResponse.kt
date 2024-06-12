package com.cpp.unsmoke.data.remote.responses.personalized

import com.google.gson.annotations.SerializedName

data class CreatePersonalizedResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<Any?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)
