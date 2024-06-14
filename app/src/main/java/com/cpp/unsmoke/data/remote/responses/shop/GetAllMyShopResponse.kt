package com.cpp.unsmoke.data.remote.responses.shop

import com.google.gson.annotations.SerializedName

data class GetAllMyShopResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItem(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("item_id")
	val itemId: String? = null,

	@field:SerializedName("img_url")
	val imgUrl: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null
)
