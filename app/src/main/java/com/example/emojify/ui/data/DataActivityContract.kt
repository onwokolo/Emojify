package com.example.emojify.ui.data

import androidx.annotation.StringRes
import com.example.emojify.base.BasePresenter
import com.example.emojify.base.BaseView

interface DataActivityContract {
    interface View : BaseView<Presenter> {


    }

    interface Presenter : BasePresenter<View> {

    }
}