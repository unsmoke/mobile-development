package com.cpp.unsmoke.data.remote.responses.activity

import com.google.gson.annotations.SerializedName

data class JournalResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItem(

	@field:SerializedName("pesan")
	val pesan: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
