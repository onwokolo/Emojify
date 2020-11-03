package com.example.emojify.ui.history

import androidx.annotation.StringRes
import com.example.emojify.base.BasePresenter
import com.example.emojify.base.BaseView

interface HistoryActivityContract {
    interface View : BaseView<Presenter> {


    }

    interface Presenter : BasePresenter<View> {

    }
}