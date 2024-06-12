package com.cpp.unsmoke.data.remote.responses.personalized

import com.google.gson.annotations.SerializedName

data class ProvinceResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItemProvince?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItemProvince(

	@field:SerializedName("province_id")
	val provinceId: Int? = null,

	@field:SerializedName("province_name")
	val provinceName: String? = null
)
