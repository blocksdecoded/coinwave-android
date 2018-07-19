package com.makeuseof.cryptocurrency.view.currency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makeuseof.core.mvp.BaseMVPFragment

class CurrencyFragment : BaseMVPFragment<CurrencyContract.Presenter>(), CurrencyContract.View {
    override var mPresenter: CurrencyContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}