package com.rchyn.prosa.components

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.rchyn.prosa.R
import com.rchyn.prosa.utils.isNotValidEmail

class EditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }


    private fun init() {
        doAfterTextChanged { editable ->
            when (inputType) {
                (InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or InputType.TYPE_CLASS_TEXT) -> {
                    val emailText = editable.toString()
                    textValidateEmail(emailText)
                }
                (InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT) -> {
                    val passwordText = editable.toString()
                    textValidatePassword(passwordText)
                }
            }
        }
    }

    private fun getTextInputLayout(): TextInputLayout? {
        var parent = parent
        while (parent is View) {
            if (parent is TextInputLayout) {
                return parent
            }
            parent = parent.getParent()
        }
        return null
    }

    private fun textValidateEmail(text: String) {
        val layout = getTextInputLayout()
        if (text.isNotValidEmail()) {
            layout?.let {
                it.apply {
                    error = context.getString(R.string.email_no_valid)
                    isErrorEnabled = true
                }
            }
        } else {
            layout?.let {
                it.apply {
                    error = null
                    isErrorEnabled = false
                }
            }
        }
    }

    private fun textValidatePassword(text: String) {
        val layout = getTextInputLayout()
        if (text.length < 6 && text.isNotEmpty()) {
            layout?.let {
                it.apply {
                    error = context.getString(R.string.password_rules_length)
                    isErrorEnabled = true
                }
            }
        } else {
            layout?.let {
                it.apply {
                    error = null
                    isErrorEnabled = false
                }
            }
        }
    }
}