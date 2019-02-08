package com.blocksdecoded.core.mvp

import com.blocksdecoded.utils.Lg

abstract class BaseMVPPresenter<T>(
        var mView: T?
): BaseMVPContract.Presenter<T> {

	init {
		injectSelfToView()
	}

    fun injectSelfToView(){
        try{
            @Suppress("LeakingThis")
            if (mView != null && mView is BaseMVPContract.View<*>){
                @Suppress("LeakingThis")
                (mView as BaseMVPContract.View<BaseMVPContract.Presenter<*>>).setPresenter(this)
            }
        }catch (e: Exception){
            Lg.d(e.message)
        }
    }

    override fun detachView() {
        try{
            if (mView != null && mView is BaseMVPContract.View<*>){
                (mView as BaseMVPContract.View<BaseMVPContract.Presenter<*>>).clearPresenter()
            }
        } catch (e: Exception) {
            Lg.d(e.message)
        }
        mView = null
    }

    override fun onStart() {
	}

	override fun onResume() {
	}

	override fun onPause() {
	}

	override fun onDestroy() {
		mView = null
	}
}