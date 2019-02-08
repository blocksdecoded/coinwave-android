package com.blocksdecoded.core.mvp

interface BaseMVPContract {
	interface View<in T>{
		fun setPresenter(presenter: T)

        fun clearPresenter()

		fun isActive(): Boolean

		fun finishView()

        fun showMessage(message: String)
	}

	interface Presenter<in T>{
        fun attachView(view: T)

        fun detachView()

		fun onStart()

		fun onResume()

		fun onPause()

		fun onDestroy()
	}
}