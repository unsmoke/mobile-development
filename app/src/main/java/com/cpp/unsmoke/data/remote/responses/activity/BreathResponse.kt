package com.cpp.unsmoke.data.remote.responses.activity

import com.google.gson.annotations.SerializedName

data class BreathResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: DataBreath? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataBreath(

	@field:SerializedName("highestDuration")
	val highestDuration: Int? = null
)
