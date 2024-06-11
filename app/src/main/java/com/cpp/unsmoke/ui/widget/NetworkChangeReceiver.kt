package com.cpp.unsmoke.ui.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.appwidget.AppWidgetManager
import android.content.ComponentName

class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Update the widget whenever a network change is detected
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, UnsmokeWidget::class.java))
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}

// Function to check network connectivity
fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}