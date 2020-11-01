package com.example.emojify.ui.splash

import com.example.emojify.base.BasePresenter
import com.example.emojify.base.BaseView

//Contracts link the activity and presenter together so they can interact with each other
//Any non-private functions MUST be declared here! This is so the activity and the presenter
//can be aware of what functions the other has, and can call on them when needed
interface SplashContract {
    interface View : BaseView<Presenter> {
        fun setupMainButton()
    }

    interface Presenter : BasePresenter<View> {

    }
}
