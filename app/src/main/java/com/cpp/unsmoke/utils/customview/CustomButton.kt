package com.cpp.unsmoke.utils.customview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class CustomButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = androidx.appcompat.R.attr.buttonStyle
) : AppCompatButton(context, attrs, defStyleAttr) {

    private var yOffset = 0f

    init {
        setOnTouchListener { _, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    yOffset = 4 * resources.displayMetrics.density
                    this.isPressed = true
                    postInvalidate()
                }
                android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> {
                    yOffset = 0f
                    this.isPressed = false
                    postInvalidate()

                    if (event.action == android.view.MotionEvent.ACTION_UP) {
                        performClick()
                    }
                }
            }
            true // Return true to indicate that the listener has consumed the event.
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.translate(0f, yOffset)
        super.onDraw(canvas)
        canvas.restore()
    }
}