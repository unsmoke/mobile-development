package com.cpp.unsmoke.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.IntentFilter
import android.graphics.Paint
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.TypefaceSpan
import android.util.Log
import android.widget.RemoteViews
import com.cpp.unsmoke.R

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
        context.registerReceiver(networkChangeReceiver, filter)
    }

    override fun onDisabled(context: Context) {
        context.unregisterReceiver(networkChangeReceiver)
    }
}

// Function to update the widget
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views: RemoteViews = if (isNetworkAvailable(context)) {
        RemoteViews(context.packageName, R.layout.unsmoke_widget)
    } else {
        RemoteViews(context.packageName, R.layout.failed_connection_widget)
    }

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}