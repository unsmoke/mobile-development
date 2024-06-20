package com.cpp.unsmoke.background

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.lifecycle.asFlow
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cpp.unsmoke.R
import com.cpp.unsmoke.data.local.preferences.GamificationPreferences
import com.cpp.unsmoke.data.local.preferences.UserPreferences
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.di.Injection
import com.cpp.unsmoke.repository.ActivityRepository
import com.cpp.unsmoke.repository.PersonalizedPlanRepository
import com.cpp.unsmoke.repository.UserDataRepository
import com.cpp.unsmoke.ui.widget.UnsmokeWidget
import kotlinx.coroutines.flow.first
import kotlin.random.Random

class MyWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    private val gamificationPreferences: GamificationPreferences by lazy {
        Injection.provideGamificationPreferences(context)
    }
    private val userPreferences: UserPreferences by lazy {
        Injection.providePersonalizedPlanRepository(context).userPreferences
    }
    private val personalizedPlanRepository: PersonalizedPlanRepository by lazy {
        Injection.providePersonalizedPlanRepository(context)
    }
    private val userDataRepository: UserDataRepository by lazy {
        Injection.provideUserDataRepository(context)
    }
    private val activityRepository: ActivityRepository by lazy {
        Injection.provideActivityRepository(context)
    }

    override suspend fun doWork(): Result {
        return try {
            val isDailyJournalDone = userPreferences.getJournalIsFilled().first()
            val isSmokeUnderLimits = userPreferences.getBreathActivityIsFilled().first()
            val isExerciseBreathDone = userPreferences.getIsmokeJournalIsFilled().first()

            Log.d("MyWorker", "isDailyJournalDone=$isDailyJournalDone, isSmokeUnderLimits=$isSmokeUnderLimits, isExerciseBreathDone=$isExerciseBreathDone")

            val cigarettesConsumed = userPreferences.getCigaretteConsumed().first()?.toInt() ?: -1

            if (isExerciseBreathDone == true) {
                // Get personalized plan
                val relapseResult = activityRepository.sendRelapseData(cigarettesConsumed).asFlow()

                var resultBreath: Result? = null
                relapseResult.collect{
                    Log.d("MyWorker", "Result from getPersonalizedPlan: $it")
                    resultBreath = when (it) {
                        is com.cpp.unsmoke.data.remote.Result.Success -> {
                            // Convert LiveData to Flow
                            val userDataFlow = userDataRepository.getUserData().asFlow()

                            // Collect the Flow
                            var result: Result? = null
                            userDataFlow.collect { dataResult ->
                                Log.d("MyWorker", "Result from getPersonalizedPlan: $dataResult")

                                result = when (dataResult) {
                                    is com.cpp.unsmoke.data.remote.Result.Success -> {
                                        // Send broadcast to update widget
                                        val intent = Intent(UnsmokeWidget.ACTION_UPDATE_WIDGET)
                                        applicationContext.sendBroadcast(intent)
                                        updateWidget(dataResult.data.data?.streakCount)
                                        Result.success()
                                    }
                                    is com.cpp.unsmoke.data.remote.Result.Error -> Result.failure()
                                    else -> Result.failure()
                                }
                            }

                            result ?: Result.failure()
                            Result.success()
                        }
                        is com.cpp.unsmoke.data.remote.Result.Error -> Result.failure()
                        else -> Result.failure()
                    }
                }
                resultBreath ?: Result.failure()
            } else {
                val relapseResult = activityRepository.sendRelapseData(-1).asFlow()

                var resultBreath: Result? = null
                relapseResult.collect{
                    Log.d("MyWorker", "Result from getPersonalizedPlan: $it")
                    resultBreath = when (it) {
                        is com.cpp.unsmoke.data.remote.Result.Success -> {
                            // Send broadcast to update widget
                            // Convert LiveData to Flow
                            val userDataFlow = userDataRepository.getUserData().asFlow()

                            // Collect the Flow
                            var result: Result? = null
                            userDataFlow.collect { dataResult ->
                                Log.d("MyWorker", "Result from getPersonalizedPlan: $dataResult")

                                result = when (dataResult) {
                                    is com.cpp.unsmoke.data.remote.Result.Success -> {
                                        // Send broadcast to update widget
                                        val intent = Intent(UnsmokeWidget.ACTION_UPDATE_WIDGET)
                                        applicationContext.sendBroadcast(intent)
                                        updateWidget(dataResult.data.data?.streakCount)
                                        Result.success()
                                    }
                                    is com.cpp.unsmoke.data.remote.Result.Error -> Result.failure()
                                    else -> Result.failure()
                                }
                            }

                            result ?: Result.failure()
                            Result.success()
                        }
                        is com.cpp.unsmoke.data.remote.Result.Error -> Result.failure()
                        else -> Result.failure()
                    }
                }
                resultBreath ?: Result.failure()
            }

//            // Convert LiveData to Flow
//            val userDataFlow = userDataRepository.getUserData().asFlow()
//
//            // Collect the Flow
//            var result: Result? = null
//            userDataFlow.collect { dataResult ->
//                Log.d("MyWorker", "Result from getPersonalizedPlan: $dataResult")
//
//                result = when (dataResult) {
//                    is com.cpp.unsmoke.data.remote.Result.Success -> {
//                        // Send broadcast to update widget
//                        val intent = Intent(UnsmokeWidget.ACTION_UPDATE_WIDGET)
//                        applicationContext.sendBroadcast(intent)
//                        updateWidget(dataResult.data.data?.streakCount)
//                        Result.success()
//                    }
//                    is com.cpp.unsmoke.data.remote.Result.Error -> Result.failure()
//                    else -> Result.failure()
//                }
//            }
//
//            result ?: Result.failure()

        } catch (e: Exception) {
            Log.e("MyWorker", "Error executing work", e)
            Result.failure()
        }
    }

    private fun updateWidget(streak: Int?) {
        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val componentName = ComponentName(applicationContext, UnsmokeWidget::class.java)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(applicationContext.packageName, R.layout.unsmoke_widget)

            // Generate random number and set to TextView
            if (streak != null) {
                views.setTextViewText(R.id.streak_widget, streak.toString())
            } else {
                views.setTextViewText(R.id.streak_widget, "-1")
            }

            // Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
