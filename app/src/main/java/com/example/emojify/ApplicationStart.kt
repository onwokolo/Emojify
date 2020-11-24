package com.example.emojify

import android.app.Application
import android.content.Context
import com.example.emojify.storage.StorageSystem
import timber.log.Timber

open class ApplicationStart : Application() {
    //Start of the application
    //We can do stuff here that needs to be started regardless of the activity
    override fun onCreate() {
        context = applicationContext
        StorageSystem.storage.setContext(context)
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }
    companion object Factory {
        lateinit var context: Context
    }
}
