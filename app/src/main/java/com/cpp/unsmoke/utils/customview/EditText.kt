package com.cpp.unsmoke.utils.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.cpp.unsmoke.R
import com.cpp.unsmoke.utils.helper.ui.EmailValidator
import com.google.android.material.textfield.TextInputLayout

class EditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
): AppCompatEditText(context, attrs){

    private var isPassError = false
    private var isEmailError = false

    private fun adjustLayout(parent: TextInputLayout?, isError: Boolean) {
        parent?.setPadding(parent.paddingLeft, parent.paddingTop, parent.paddingRight, if (isError) 20 else 10)
    }

    init {
        when (id) {
            R.id.signup_edt_password, R.id.edt_login_password, R.id.signup_edt_confirm_password, R.id.edt_new_password, R.id.edt_confirm_new_password -> {
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        val parent = parent.parent as? TextInputLayout

                        if (isPassError){
                            parent?.startIconDrawable?.setTint(ContextCompat.getColor(context, R.color.color_error))
                        } else {
                            parent?.startIconDrawable?.setTint(ContextCompat.getColor(context, R.color.normal_green))
                        }

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        val parent = parent.parent as? TextInputLayout
                        if (s.toString().length < 8) {
                            isPassError = true
                            parent?.error = context.getString(R.string.password_alert)
                            parent?.boxStrokeColor =
                                ContextCompat.getColor(context, R.color.color_error)
                            parent?.startIconDrawable?.setTint(ContextCompat.getColor(context, R.color.color_error))
                        } else {
                            isPassError = false
                            parent?.error = null
                            parent?.isErrorEnabled = false
                            parent?.boxStrokeColor =
                                ContextCompat.getColor(context, R.color.normal_green)

                        }
                    }

                    override fun afterTextChanged(s: Editable?) {
                        val parent = parent.parent as? TextInputLayout

                        if (isPassError){
                            parent?.startIconDrawable?.setTint(ContextCompat.getColor(context, R.color.color_error))
                        } else {
                            parent?.startIconDrawable?.setTint(ContextCompat.getColor(context, R.color.normal_green))
                        }
                    }

                })

                // Add OnFocusChangeListener to revert the start icon color when focus is lost
                onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
                    val parent = parent.parent as? TextInputLayout
                    if (!hasFocus && !isPassError) {
                        parent?.startIconDrawable?.setTint(ContextCompat.getColor(context, R.color.neutral_50))
                    }
                }
            }

            R.id.edt_signup_email, R.id.edt_login_email, R.id.edt_forgot_password -> {
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        val parent = parent.parent as? TextInputLayout

                        if (isEmailError){
                            parent?.startIconDrawable?.setTint(ContextCompat.getColor(context, R.color.color_error))
                        } else {
                            parent?.startIconDrawable?.setTint(ContextCompat.getColor(context, R.color.normal_green))
                        }
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        val parent = parent.parent as? TextInputLayout
                        if (!EmailValidator.validate(s.toString())) {
                            isEmailError = true
                            parent?.error = context.getString(R.string.invalid_email)
                            parent?.boxStrokeColor =
                                ContextCompat.getColor(context, R.color.color_error)
                            parent?.startIconDrawable?.setTint(ContextCompat.getColor(context, R.color.color_error))

                            adjustLayout(parent, true)
                        } else {
                            isEmailError = false
                            parent?.error = null
                            parent?.isErrorEnabled = false
                            parent?.boxStrokeColor =
                                ContextCompat.getColor(context, R.color.normal_green)
                            adjustLayout(parent, false)
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {
                        val parent = parent.parent as? TextInputLayout

                        if (isEmailError){
                            parent?.startIconDrawable?.setTint(ContextCompat.getColor(context, R.color.color_error))
                        } else {
                            parent?.startIconDrawable?.setTint(ContextCompat.getColor(context, R.color.normal_green))
                        }
                    }

                })

                // Add OnFocusChangeListener to revert the start icon color when focus is lost
                onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
                    val parent = parent.parent as? TextInputLayout
                    if (!hasFocus && !isEmailError) {
                        parent?.startIconDrawable?.setTint(ContextCompat.getColor(context, R.color.neutral_50))
                    }
                }
            }

            R.id.edt_signup_name -> {
                // Add OnFocusChangeListener to revert the start icon color when focus is lost
                onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
                    val parent = parent.parent as? TextInputLayout
                    if (!hasFocus) {
                        parent?.startIconDrawable?.setTint(ContextCompat.getColor(context, R.color.neutral_50))
                    }
                }
            }
        }

        // Add OnTouchListener to change the start icon color on touch
        setOnTouchListener{ v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val parent = parent.parent as? TextInputLayout

                if (isPassError || isEmailError){
                    parent?.startIconDrawable?.setTint(ContextCompat.getColor(context, R.color.color_error))
                } else {
                    parent?.startIconDrawable?.setTint(ContextCompat.getColor(context, R.color.normal_green))
                }
            } else if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
            }
            false
        }

    }
}