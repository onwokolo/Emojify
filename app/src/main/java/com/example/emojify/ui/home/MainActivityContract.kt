package com.example.emojify.ui.home

import androidx.annotation.StringRes
import com.example.emojify.base.BasePresenter
import com.example.emojify.base.BaseView

interface MainActivityContract {
    interface View : BaseView<Presenter> {


    }

    interface Presenter : BasePresenter<View> {

    }
}