package com.example.emojify.di

import com.example.emojify.ui.data.DataActivity
import com.example.emojify.ui.data.DataActivityContract
import com.example.emojify.ui.data.DataActivityPresenter
import com.example.emojify.ui.history.HistoryActivity
import com.example.emojify.ui.history.HistoryActivityContract
import com.example.emojify.ui.history.HistoryActivityPresenter
import com.example.emojify.ui.splash.SplashActivity
import com.example.emojify.ui.splash.SplashContract
import com.example.emojify.ui.splash.SplashPresenter
import com.example.emojify.ui.home.MainActivity
import com.example.emojify.ui.home.MainActivityContract
import com.example.emojify.ui.home.MainActivityPresenter
import com.example.emojify.ui.log.LogActivity
import com.example.emojify.ui.log.LogActivityContract
import com.example.emojify.ui.log.LogActivityPresenter
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
    scope(named<LogActivity>()) {
        scoped {
            LogActivityPresenter() as LogActivityContract.Presenter
        }
    }
    scope(named<DataActivity>()) {
        scoped {
            DataActivityPresenter() as DataActivityContract.Presenter
        }
    }
    scope(named<HistoryActivity>()) {
        scoped {
            HistoryActivityPresenter() as HistoryActivityContract.Presenter
        }
    }
}
