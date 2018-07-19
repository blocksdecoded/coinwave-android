package com.makeuseof.core.mvp

import android.support.v4.app.Fragment
import com.makeuseof.utils.showShortToast

abstract class BaseMVPFragment<T>: Fragment(), BaseMVPContract.View<T> {
    abstract var mPresenter: T?

	override fun isActive(): Boolean = isAdded

	override fun setPresenter(presenter: T) {
		mPresenter = presenter
		if (mPresenter is BaseMVPContract.Presenter<*>)
			(mPresenter as BaseMVPContract.Presenter<*>).onStart()
	}

	override fun onResume() {
		super.onResume()
		if (mPresenter is BaseMVPContract.Presenter<*>)
			(mPresenter as BaseMVPContract.Presenter<*>).onResume()
	}

	override fun onPause() {
		super.onPause()
		if (mPresenter is BaseMVPContract.Presenter<*>)
			(mPresenter as BaseMVPContract.Presenter<*>).onPause()
	}

	override fun onDestroy() {
		super.onDestroy()
		if (mPresenter is BaseMVPContract.Presenter<*>)
			(mPresenter as BaseMVPContract.Presenter<*>).onDestroy()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		if (mPresenter is BaseMVPContract.Presenter<*>)
			(mPresenter as BaseMVPContract.Presenter<*>).onDestroy()
	}

	override fun finishView() {
		activity?.finish()
	}

    override fun clearPresenter() {
        mPresenter = null
    }

    fun runOnUi(body: () -> Unit){
        activity?.runOnUiThread { body.invoke() }
    }

    override fun showMessage(message: String) {
        showShortToast(context, message)
    }
}