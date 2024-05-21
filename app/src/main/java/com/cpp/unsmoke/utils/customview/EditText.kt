package com.cpp.unsmoke.utils.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.cpp.unsmoke.R
import com.cpp.unsmoke.utils.helper.ui.EmailValidator
import com.google.android.material.textfield.TextInputLayout

class EditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
): AppCompatEditText(context, attrs){
    init {
        when (id) {
            R.id.signup_edt_password, R.id.edt_login_password -> {
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        val parent = parent.parent as? TextInputLayout
                        if (s.toString().length < 8) {
                            parent?.error = context.getString(R.string.password_alert)
                            parent?.boxStrokeColor =
                                ContextCompat.getColor(context, R.color.color_error)
                        } else {
                            parent?.error = null
                            parent?.boxStrokeColor =
                                ContextCompat.getColor(context, R.color.normal_green)
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }

                })
            }

            R.id.edt_signup_email, R.id.edt_login_email -> {
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        val parent = parent.parent as? TextInputLayout
                        if (!EmailValidator.validate(s.toString())) {
                            parent?.error = context.getString(R.string.invalid_email)
                            parent?.boxStrokeColor =
                                ContextCompat.getColor(context, R.color.color_error)
                        } else {
                            parent?.error = null
                            parent?.boxStrokeColor =
                                ContextCompat.getColor(context, R.color.normal_green)
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }

                })
            }
        }

        // Add OnTouchListener to change the start icon color on touch
        setOnTouchListener{ v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val parent = parent.parent as? TextInputLayout
                parent?.startIconDrawable?.setTint(ContextCompat.getColor(context, R.color.normal_green))
            } else if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
            }
            false
        }

        // Add OnFocusChangeListener to revert the start icon color when focus is lost
        onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            val parent = parent.parent as? TextInputLayout
            if (!hasFocus) {
                parent?.startIconDrawable?.setTint(ContextCompat.getColor(context, R.color.neutral_50))
            }
        }
    }
}