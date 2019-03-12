package com.blocksdecoded.core.mvp

interface BaseMvpContract {
    interface View<T : Presenter<*>> {
        val presenter: T

        fun finishView()

        fun showMessage(message: String)
    }

    interface Presenter<T> {
        var view: T?

        fun onStart()

        fun onResume()

        fun onPause()

        fun onDestroy()
    }
}