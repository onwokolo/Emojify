package com.example.emojify.ui.splash

class SplashPresenter :
    SplashContract.Presenter {

    var view: SplashContract.View? = null //

    override fun takeView(view: SplashContract.View) {
        this.view = view //acquire the activity
        /*if (sharedPreferenceHelper.getOnboardingShown()) {    MAY BE NEEDED
            view.openHomeScreen()
        } else {
            view.openOnboarding()
        }*/

    }

    override fun dropView() {
        view = null //Sets view to null. This is important as we don't want the presenter to hold onto a dead activity, and helps prevent memory leaks
    }
}
