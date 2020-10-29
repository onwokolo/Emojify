package com.example.emojify.base

interface BasePresenter<T> {

    fun takeView(view: T)

    fun dropView()
}
