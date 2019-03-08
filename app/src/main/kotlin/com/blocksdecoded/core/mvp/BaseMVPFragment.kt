package com.blocksdecoded.core.mvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder
import com.blocksdecoded.utils.extensions.inflate
import com.blocksdecoded.utils.logE
import com.blocksdecoded.utils.showShortToast

abstract class BaseMVPFragment<T> : Fragment(), BaseMVPContract.View<T> {
    abstract var mPresenter: T?
    abstract val layoutId: Int

    private var mUnbinder: Unbinder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = try {
        container.inflate(layoutId)?.also {
            mUnbinder = ButterKnife.bind(this, it)
            initView(it)
        }
    } catch (e: Exception) {
        logE(e)
        null
    }

    abstract fun initView(rootView: View)

    @CallSuper
    override fun onResume() {
        super.onResume()
        if (mPresenter is BaseMVPContract.Presenter<*>)
            (mPresenter as BaseMVPContract.Presenter<*>).onResume()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        if (mPresenter is BaseMVPContract.Presenter<*>)
            (mPresenter as BaseMVPContract.Presenter<*>).onPause()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        if (mPresenter is BaseMVPContract.Presenter<*>)
            (mPresenter as BaseMVPContract.Presenter<*>).onDestroy()
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        if (mPresenter is BaseMVPContract.Presenter<*>)
            (mPresenter as BaseMVPContract.Presenter<*>).onDestroy()

        mUnbinder?.unbind()
    }

    override fun onDetach() {
        super.onDetach()
    }

    //region Base Contract

    override fun isActive(): Boolean = isAdded

    @CallSuper
    override fun setPresenter(presenter: T) {
        mPresenter = presenter
        if (mPresenter is BaseMVPContract.Presenter<*>)
            (mPresenter as BaseMVPContract.Presenter<*>).onStart()
    }

    override fun finishView() {
        activity?.finish()
    }

    @CallSuper
    override fun clearPresenter() {
        mPresenter = null
    }

    override fun showMessage(message: String) {
        showShortToast(context, message)
    }

    //endregion

    fun runOnUi(body: () -> Unit) {
        activity?.runOnUiThread { body.invoke() }
    }
}