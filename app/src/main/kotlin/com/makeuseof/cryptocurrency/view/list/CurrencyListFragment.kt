package com.makeuseof.cryptocurrency.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makeuseof.core.mvp.BaseMVPFragment
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.data.model.CurrencyEntity
import com.makeuseof.utils.inflate

class CurrencyListFragment : BaseMVPFragment<CurrencyListContract.Presenter>(), CurrencyListContract.View {
    override var mPresenter: CurrencyListContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = container.inflate(R.layout.fragment_currency_list)

        initView(rootView)

        return rootView
    }

    private fun initView(rootView: View?) {

    }

    override fun showCurrencies(currencies: List<CurrencyEntity>) {

    }

    override fun showNetworkError() {

    }
}