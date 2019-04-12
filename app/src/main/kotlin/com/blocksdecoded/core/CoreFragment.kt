package com.blocksdecoded.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder
import com.blocksdecoded.utils.extensions.inflate
import com.blocksdecoded.utils.logE

abstract class CoreFragment : Fragment() {
    abstract val layoutId: Int

    private var mUnbinder: Unbinder? = null

    abstract fun initView(rootView: View)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = try {
        container?.inflate(layoutId)?.also {
            mUnbinder = ButterKnife.bind(this, it)
            initView(it)
        }
    } catch (e: Exception) {
        logE(e)
        null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder?.unbind()
    }
}