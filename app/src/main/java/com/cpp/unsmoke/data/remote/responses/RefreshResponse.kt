package com.cpp.unsmoke.data.remote.responses

import com.google.gson.annotations.SerializedName

data class RefreshResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: RefreshData? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class RefreshData(

	@field:SerializedName("accessToken")
	val accessToken: String? = null
)
