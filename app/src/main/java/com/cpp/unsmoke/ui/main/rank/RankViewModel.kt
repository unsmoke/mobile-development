package com.cpp.unsmoke.ui.main.rank

import LeaderboardRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cpp.unsmoke.data.remote.responses.leaderboard.LeaderboardItem

class RankViewModel(private val leaderboardRepository: LeaderboardRepository): ViewModel() {

    val rank: LiveData<PagingData<LeaderboardItem>> = leaderboardRepository.getLeaderboard().cachedIn(viewModelScope)
}