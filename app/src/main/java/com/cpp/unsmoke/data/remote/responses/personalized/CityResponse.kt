package com.cpp.unsmoke.data.remote.responses.personalized

import com.google.gson.annotations.SerializedName

data class CityResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItemCity?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItemCity(

	@field:SerializedName("city_name")
	val cityName: String? = null,

	@field:SerializedName("province_id")
	val provinceId: Int? = null,

	@field:SerializedName("city_id")
	val cityId: Int? = null
)
