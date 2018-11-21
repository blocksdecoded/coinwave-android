package com.makeuseof.core.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.makeuseof.utils.inflate
import com.makeuseof.utils.showShortToast

abstract class BaseMVPFragment<T>: Fragment(), BaseMVPContract.View<T> {
    abstract var mPresenter: T?
    abstract val layoutId: Int

    private var mUnbinder: Unbinder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = try {
        container.inflate(layoutId)?.also {
            mUnbinder = ButterKnife.bind(this, it)
            initView(it)
        }
    } catch (e: Exception) {
        null
    }

    abstract fun initView(rootView: View)

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

        mUnbinder?.unbind()
	}

    //region Base Contract

    override fun isActive(): Boolean = isAdded

    override fun setPresenter(presenter: T) {
        mPresenter = presenter
        if (mPresenter is BaseMVPContract.Presenter<*>)
            (mPresenter as BaseMVPContract.Presenter<*>).onStart()
    }

	override fun finishView() {
		activity?.finish()
	}

    override fun clearPresenter() {
        mPresenter = null
    }

    override fun showMessage(message: String) {
        showShortToast(context, message)
    }

    //endregion

    fun runOnUi(body: () -> Unit){
        activity?.runOnUiThread { body.invoke() }
    }
}