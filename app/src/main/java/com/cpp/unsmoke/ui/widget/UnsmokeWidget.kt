package com.cpp.unsmoke.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
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

// Function to update the widget
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views = RemoteViews(context.packageName, R.layout.unsmoke_widget)

    try {
        // Load the custom font from assets
        val typeface = Typeface.createFromAsset(context.assets, "font/poppins_bold.ttf")
        Log.d("UnsmokeWidget", "Font loaded successfully.")

        // Create a SpannableString with the custom font for the first TextView
        val streakText = "14"
        val spannableString = SpannableString(streakText).apply {
            setSpan(CustomTypefaceSpan(typeface), 0, streakText.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        Log.d("UnsmokeWidget", "SpannableString created successfully.")

        // Set the SpannableString to the TextView
        views.setTextViewText(R.id.streak_widget, spannableString)
        Log.d("UnsmokeWidget", "TextView updated successfully.")

        // Create a SpannableString with the custom font for the second TextView
        val motivationalText = "Semangat Broohh \nRarrrwhhhh!!"
        val spannableMotivational = SpannableString(motivationalText).apply {
            setSpan(CustomTypefaceSpan(typeface), 0, motivationalText.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        views.setTextViewText(R.id.motivational_text, spannableMotivational)

    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("UnsmokeWidget", "Error loading font: ${e.message}")
    }

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
    Log.d("UnsmokeWidget", "Widget updated successfully.")
}

// CustomTypefaceSpan class to apply the custom typeface
class CustomTypefaceSpan(private val newType: Typeface) : TypefaceSpan("") {
    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeFace(ds, newType)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, newType)
    }

    private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
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