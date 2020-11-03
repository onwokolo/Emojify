package com.example.emojify.ui.log

import androidx.annotation.StringRes
import com.example.emojify.base.BasePresenter
import com.example.emojify.base.BaseView

interface LogActivityContract {
    interface View : BaseView<Presenter> {


    }

    interface Presenter : BasePresenter<View> {

    }
}