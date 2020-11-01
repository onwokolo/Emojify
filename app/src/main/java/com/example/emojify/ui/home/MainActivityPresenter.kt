package com.example.emojify.ui.home

class MainActivityPresenter: MainActivityContract.Presenter {

    var view: MainActivityContract.View? = null

    override fun takeView(view: MainActivityContract.View) {
        this.view = view
    }

    override fun dropView() {
        view = null
    }
}
