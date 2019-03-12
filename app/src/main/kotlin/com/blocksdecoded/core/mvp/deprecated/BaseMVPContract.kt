package com.blocksdecoded.core.mvp.deprecated

@Deprecated("Use newer version BaseMvpContract")
interface BaseMVPContract {
    interface View<T> {
        fun setPresenter(presenter: T)

        fun clearPresenter()

        fun isActive(): Boolean

        fun finishView()

        fun showMessage(message: String)
    }

    interface Presenter<in T> {
        fun attachView(view: T)

        fun detachView()

        fun onStart()

        fun onResume()

        fun onPause()

        fun onDestroy()
    }
}