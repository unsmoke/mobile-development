package com.cpp.unsmoke.data.remote.responses.shop

import com.google.gson.annotations.SerializedName

data class EquipItemResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: DataEquipItem? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataEquipItem(

	@field:SerializedName("current_lung")
	val currentLung: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("item_id")
	val itemId: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
