package com.example.photoapp

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber

class App: Application() {

    init {
        app = this
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

    companion object {
        lateinit var app: App
        fun context(): Context = app.applicationContext
    }
}