package com.example.emojify.ui.history

class HistoryActivityPresenter: HistoryActivityContract.Presenter {

    var view: HistoryActivityContract.View? = null

    override fun takeView(view: HistoryActivityContract.View) {
        this.view = view
    }

    override fun dropView() {
        view = null
    }
}
