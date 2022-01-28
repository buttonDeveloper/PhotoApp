package com.example.photoapp

import android.content.Context
import android.net.Uri
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.example.photoapp.room.GalleryItem


fun dimenFloat(@DimenRes resInt: Int) = App.context().resources.getDimensionPixelSize(resInt).toFloat()

fun getDrawable(context: Context, @DrawableRes drawableRes: Int) = ContextCompat.getDrawable(context, drawableRes)

fun getString(context: Context, @StringRes stringRes: Int) = context.resources.getString(stringRes)

fun EditText.onSubmit(func: () -> Unit) {setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) { func() }
        true
    }
}
