package com.cpp.unsmoke.data.remote.responses.shop

import com.google.gson.annotations.SerializedName

data class CreateItemResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: DataCreateItem? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataCreateItem(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("item_id")
	val itemId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null
)
