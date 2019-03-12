package com.blocksdecoded.core.mvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder
import com.blocksdecoded.utils.extensions.inflate
import com.blocksdecoded.utils.logE
import com.blocksdecoded.utils.showShortToast

abstract class BaseMvpFragment<T: BaseMvpContract.Presenter<*>> : Fragment(), BaseMvpContract.View<T> {
    abstract val layoutId: Int

    private var mUnbinder: Unbinder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = try {
        container.inflate(layoutId)?.also {
            mUnbinder = ButterKnife.bind(this, it)
            initView(it)
        }
    } catch (e: Exception) {
        logE(e)
        TextView(context).apply { text = "${javaClass.simpleName} layout initialization error!" }
    }

    abstract fun initView(rootView: View)

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