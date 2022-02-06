package com.example.photoapp.listener

import android.content.Context
import android.os.*
import android.view.MotionEvent
import android.view.View
import timber.log.Timber
import android.os.Handler
import com.example.photoapp.App

class ItemTouchListener(private val callback: TouchCallback, private val isDeleteMode: Boolean) : View.OnTouchListener {

    init {
        Timber.d("ItemTouchListener isDeleteMode = $isDeleteMode")
    }

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable {
        Timber.d("Runnable")
        callback.onLongClick()
        vibrate()
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                Timber.d("DOWN. isDeleteMode: $isDeleteMode")
                return if (isDeleteMode) {
                    true
                }else {
                    //checking 3sec press down
                    handler.postDelayed(runnable, 3000)
                    true
                }
            }
            MotionEvent.ACTION_UP -> {
                Timber.d("UP. isDeleteMode: $isDeleteMode")
                return if (isDeleteMode) {
                    performClick()
                    true
                }
                else {
                    performClick()
                    handler.removeCallbacks(runnable)
                    true
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                Timber.d("CANCEL. isDeleteMode: $isDeleteMode")
                if (!isDeleteMode) handler.removeCallbacks(runnable)
                return true
            }
        }
        return false
    }

    private fun performClick() {
        Timber.d("performClick")
        callback.onClick()
    }

    private fun vibrate() {
        (App.context().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            else vibrate(100)
        }
    }
}

