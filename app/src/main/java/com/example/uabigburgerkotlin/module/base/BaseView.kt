package com.example.uabigburgerkotlin.module.base

import io.reactivex.disposables.Disposable

interface BaseView {
    fun showProgress()
    fun hideProgress()
    fun onDestroy(disposable: Disposable)
}