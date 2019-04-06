package com.blocksdecoded.core.mvp

import androidx.annotation.CallSuper
import butterknife.Unbinder
import com.blocksdecoded.core.CoreFragment
import com.blocksdecoded.utils.showShortToast

abstract class BaseMvpFragment<T : BaseMvpContract.Presenter<*>> : CoreFragment(), BaseMvpContract.View<T> {
    private var mUnbinder: Unbinder? = null

    @CallSuper
    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
        mUnbinder?.unbind()
    }

    //region Base Contract

    @CallSuper
    override fun finishView() {
        activity?.finish()
    }

    override fun showMessage(message: String) {
        showShortToast(context, message)
    }

    //endregion

    fun runOnUi(body: () -> Unit) {
        activity?.runOnUiThread { body.invoke() }
    }
}