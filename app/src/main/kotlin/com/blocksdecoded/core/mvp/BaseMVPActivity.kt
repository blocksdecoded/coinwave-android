package com.blocksdecoded.core.mvp

import androidx.appcompat.app.AppCompatActivity
import com.blocksdecoded.utils.showShortToast

/**
 * Created by Tameki on 1/15/18.
 */
abstract class BaseMVPActivity <T> : AppCompatActivity(), BaseMVPContract.View<T> {
    abstract var mPresenter: T?

    override fun isActive() = true

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

    override fun finishView() {
        finish()
    }

    override fun clearPresenter() {
        mPresenter = null
    }

    override fun showMessage(message: String) {
        showShortToast(this, message)
    }
}