package com.example.emojify.ui.log

class LogActivityPresenter: LogActivityContract.Presenter {

    var view: LogActivityContract.View? = null

    override fun takeView(view: LogActivityContract.View) {
        this.view = view
    }

    override fun dropView() {
        view = null
    }
}
