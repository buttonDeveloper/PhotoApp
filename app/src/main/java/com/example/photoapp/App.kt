package com.example.photoapp

import android.app.Application
import android.content.Context
import timber.log.Timber

class App: Application() {

    init {
        app = this
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

    companion object {
        lateinit var app: App
        fun context(): Context = app.applicationContext
    }
}