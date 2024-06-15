package com.cpp.unsmoke.repository.leaderboard

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cpp.unsmoke.data.remote.retrofit.ApiService
import com.cpp.unsmoke.data.remote.responses.leaderboard.LeaderboardItem
import kotlinx.coroutines.runBlocking
import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.data.local.preferences.UserPreferences
import kotlinx.coroutines.flow.first

class LeaderboardPagingSource(
    private val apiService: ApiService,
    private val loginPreferences: LoginPreferences,
    private val userPreferences: UserPreferences
) : PagingSource<Int, LeaderboardItem>() {

    override fun getRefreshKey(state: PagingState<Int, LeaderboardItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LeaderboardItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val token = runBlocking { loginPreferences.getToken().first() }
            val userProv = runBlocking { userPreferences.getProv().first() }
            val response = apiService.getLeaderboardByProvince("Bearer $token", userProv.toString().toInt(), position, params.loadSize).data
            val leaderboardItems = response?.filterNotNull() ?: emptyList()
            LoadResult.Page(
                data = leaderboardItems,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (leaderboardItems.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}