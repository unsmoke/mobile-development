package com.cpp.unsmoke.data.remote.responses.activity

import com.google.gson.annotations.SerializedName

data class RelapseResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("streak_count")
	val streakCount: Int? = null
)
