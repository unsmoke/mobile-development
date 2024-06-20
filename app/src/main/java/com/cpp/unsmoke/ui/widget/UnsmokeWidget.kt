package com.cpp.unsmoke.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.work.ListenableWorker.Result
import com.cpp.unsmoke.R
import com.cpp.unsmoke.di.Injection
import com.cpp.unsmoke.repository.UserDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

/**
 * Implementation of App Widget functionality.
 */
class UnsmokeWidget : AppWidgetProvider() {

    private lateinit var networkChangeReceiver: NetworkChangeReceiver

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        networkChangeReceiver = NetworkChangeReceiver()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
//        context.registerReceiver(networkChangeReceiver, filter)

        // Register broadcast receiver for updating widget
        val widgetUpdateFilter = IntentFilter(ACTION_UPDATE_WIDGET)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(widgetUpdateReceiver, widgetUpdateFilter, Context.RECEIVER_EXPORTED)
        } else {
            context.registerReceiver(widgetUpdateReceiver, widgetUpdateFilter)
        }
    }

    override fun onDisabled(context: Context) {
        context.unregisterReceiver(networkChangeReceiver)
        context.unregisterReceiver(widgetUpdateReceiver)
    }

    private val widgetUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ACTION_UPDATE_WIDGET) {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(
                    ComponentName(context, UnsmokeWidget::class.java)
                )
                Log.d("UnsmokeWidget", "Broadcast received, updating widgets")
                onUpdate(context, appWidgetManager, appWidgetIds)
            }
        }
    }

    companion object {
        const val ACTION_UPDATE_WIDGET = "com.cpp.unsmoke.UPDATE_WIDGET"
    }
}

// Function to update the widget
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    CoroutineScope(Dispatchers.Main).launch {
        val views = RemoteViews(context.packageName, R.layout.unsmoke_widget)

        val userDataRepository = Injection.provideUserDataRepository(context)
        val result = withContext(Dispatchers.IO) {
            userDataRepository.getUserData()
        }

        result.observeForever {
            when (it) {
                is com.cpp.unsmoke.data.remote.Result.Success -> {
                    views.setTextViewText(R.id.streak_widget, it.data.data?.streakCount.toString())
                }
                is com.cpp.unsmoke.data.remote.Result.Error -> {
                    views.setTextViewText(R.id.streak_widget, "-1")
                }
                else -> {
                    views.setTextViewText(R.id.streak_widget, "1")
                }
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
