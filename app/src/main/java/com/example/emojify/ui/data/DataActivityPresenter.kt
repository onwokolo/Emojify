package com.example.emojify.ui.data

class DataActivityPresenter: DataActivityContract.Presenter {

    var view: DataActivityContract.View? = null

    override fun takeView(view: DataActivityContract.View) {
        this.view = view
    }

    override fun dropView() {
        view = null
    }
}
