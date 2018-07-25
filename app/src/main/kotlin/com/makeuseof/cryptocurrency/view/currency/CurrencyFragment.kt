package com.makeuseof.cryptocurrency.view.currency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makeuseof.core.mvp.BaseMVPFragment
import com.makeuseof.cryptocurrency.R
import com.makeuseof.utils.inflate

class CurrencyFragment : BaseMVPFragment<CurrencyContract.Presenter>(), CurrencyContract.View {
    override var mPresenter: CurrencyContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = container.inflate(R.layout.fragment_currency_info)

        initView(rootView)

        return rootView
    }

    private fun initView(rootView: View?){

    }
}