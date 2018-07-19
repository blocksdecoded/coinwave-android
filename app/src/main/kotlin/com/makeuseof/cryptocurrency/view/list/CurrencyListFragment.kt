package com.makeuseof.cryptocurrency.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makeuseof.core.mvp.BaseMVPFragment

class CurrencyListFragment : BaseMVPFragment<CurrencyListContract.Presenter>(), CurrencyListContract.View {
    override var mPresenter: CurrencyListContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}