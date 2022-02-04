package com.example.photoapp

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat


fun getDrawable(context: Context, @DrawableRes drawableRes: Int) = ContextCompat.getDrawable(context, drawableRes)

fun EditText.onSubmit(func: () -> Unit) {setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) { func() }
        true
    }
}
