import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import kotlinx.coroutines.flow.Flow
import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.data.local.preferences.UserPreferences
import com.cpp.unsmoke.data.remote.retrofit.ApiService
import com.cpp.unsmoke.data.remote.responses.leaderboard.LeaderboardItem
import com.cpp.unsmoke.repository.leaderboard.LeaderboardPagingSource

class LeaderboardRepository(
    private val apiService: ApiService,
    private val loginPreferences: LoginPreferences,
    private val userPreferences: UserPreferences
) {

    fun getLeaderboard(): LiveData<PagingData<LeaderboardItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { LeaderboardPagingSource(apiService, loginPreferences, userPreferences) }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: LeaderboardRepository? = null

        fun getInstance(
            apiService: ApiService,
            preferences: LoginPreferences,
            userPreferences: UserPreferences
        ): LeaderboardRepository =
            instance ?: synchronized(this) {
                instance ?: LeaderboardRepository(apiService, preferences, userPreferences).also {
                    instance = it
                }
            }
    }
}