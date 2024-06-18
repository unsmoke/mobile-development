package com.cpp.unsmoke.data.remote.responses.leaderboard

import com.google.gson.annotations.SerializedName

data class LeaderboardResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<LeaderboardItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class LeaderboardItem(

	@field:SerializedName("rank")
	val rank: Int? = null,

	@field:SerializedName("exp")
	val exp: Int? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("profile_url")
	val profileUrl: String? = null,

	@field:SerializedName("current_lung")
	val lungUrl: String? = null
)
