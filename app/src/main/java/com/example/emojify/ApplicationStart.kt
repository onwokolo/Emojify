package com.example.emojify

import android.app.Application
import android.content.Context
import com.example.emojify.di.appModule
import com.example.emojify.storage.StorageSystem
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

open class ApplicationStart : Application() {
    //Start of the application
    //We can do stuff here that needs to be started regardless of the activity
    //Despite it saying 'Class "ApplicationStart" is never used', it is indeed used in 'AndroidManifest.xml'
    override fun onCreate() {
        context = applicationContext
        StorageSystem.storage.setContext(context)
        super.onCreate()

        //if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree()) //Sets up timber. I've no idea what that does, but we can use it to create log statements for debugging
        //}

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        startKoin {//No idea what this does either, but I know the result is a single instance of each presenter class that is important for later
            androidContext(this@ApplicationStart)
            modules(
                listOf(
                    appModule
                )
            )
        }
    }
    companion object Factory {
        lateinit var context: Context
    }
}
