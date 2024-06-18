package com.cpp.unsmoke.ui.main.rank

import LeaderboardRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.leaderboard.LeaderboardItem
import com.cpp.unsmoke.data.remote.responses.userprofile.UserDetailDataResponse
import com.cpp.unsmoke.repository.UserDataRepository

class RankViewModel(
    private val userDataRepository: UserDataRepository,
    private val leaderboardRepository: LeaderboardRepository
) : ViewModel() {

    private val _refreshTrigger = MutableLiveData<Unit>()
    private val refreshTrigger: LiveData<Unit> get() = _refreshTrigger

    val rank: LiveData<PagingData<LeaderboardItem>> = refreshTrigger.switchMap {
        leaderboardRepository.getLeaderboard().cachedIn(viewModelScope)
    }

    fun refreshData() {
        _refreshTrigger.value = Unit
    }

    fun getUserData(): LiveData<Result<UserDetailDataResponse>> {
        return userDataRepository.getUserData()
    }
}