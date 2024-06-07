package com.cpp.unsmoke.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TypefaceSpan
import android.widget.RemoteViews
import com.cpp.unsmoke.R

/**
 * Implementation of App Widget functionality.
 */
class UnsmokeWidget : AppWidgetProvider() {
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
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views = RemoteViews(context.packageName, R.layout.unsmoke_widget)

    // Load the custom font from assets
    val typeface = Typeface.createFromAsset(context.assets, "font/poppins_bold.ttf")

    // Create a SpannableString with the custom font
    val streakText = "14"
    val spannableString = SpannableString(streakText).apply {
        setSpan(CustomTypefaceSpan(typeface), 0, streakText.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    }

    // Set the SpannableString to the TextView
    views.setTextViewText(R.id.streak_widget, spannableString)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

// Extension function to set typeface on TextView in RemoteViews
class CustomTypefaceSpan(private val newType: Typeface) : TypefaceSpan("") {
    override fun updateDrawState(textPaint: android.text.TextPaint) {
        applyCustomTypeFace(textPaint, newType)
    }

    override fun updateMeasureState(paint: android.text.TextPaint) {
        applyCustomTypeFace(paint, newType)
    }

    private fun applyCustomTypeFace(paint: android.text.TextPaint, tf: Typeface) {
        val oldStyle: Int
        val old = paint.typeface
        oldStyle = old?.style ?: 0

        val fake = oldStyle and tf.style.inv()
        if (fake and Typeface.BOLD != 0) {
            paint.isFakeBoldText = true
        }
        if (fake and Typeface.ITALIC != 0) {
            paint.textSkewX = -0.25f
        }

        paint.typeface = tf
    }
}