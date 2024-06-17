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
import com.cpp.unsmoke.R
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
    val views: RemoteViews = RemoteViews(context.packageName, R.layout.unsmoke_widget)

    // Generate random number and set to TextView
    val randomNumber = Random.nextInt(100)
    Log.d("UnsmokeWidget", "Random number: $randomNumber")
    views.setTextViewText(R.id.streak_widget, randomNumber.toString())

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
