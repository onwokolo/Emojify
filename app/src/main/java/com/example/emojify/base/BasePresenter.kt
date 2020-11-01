package com.example.emojify.base

//This is the base presenter class
//They take a view (Activity) and perform logic
//All the background stuff happens here. In addition,
//Should the background logic need to change the foreground appearance,
//we can call view.doSomething() where doSomething is a method in Activity
//that would change the appearance in some way.
interface BasePresenter<T> {

    fun takeView(view: T)

    fun dropView()
}
