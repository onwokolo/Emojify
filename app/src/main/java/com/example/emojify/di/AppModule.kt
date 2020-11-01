package com.example.emojify.di

import com.example.emojify.ui.splash.SplashActivity
import com.example.emojify.ui.splash.SplashContract
import com.example.emojify.ui.splash.SplashPresenter
import com.example.emojify.ui.home.MainActivity
import com.example.emojify.ui.home.MainActivityContract
import com.example.emojify.ui.home.MainActivityPresenter
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    scope(named<MainActivity>()) {
        scoped {
            MainActivityPresenter()
                    as MainActivityContract.Presenter
        }
    }

    scope(named<SplashActivity>()) {
        scoped {
            SplashPresenter() as SplashContract.Presenter
        }
    }
}
