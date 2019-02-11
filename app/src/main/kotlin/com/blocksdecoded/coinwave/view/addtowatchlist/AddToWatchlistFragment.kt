package com.blocksdecoded.coinwave.view.addtowatchlist

import android.view.View
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.core.mvp.BaseMVPFragment

class AddToWatchlistFragment : BaseMVPFragment<AddToWatchlistContract.Presenter>(), AddToWatchlistContract.View {
    override val layoutId: Int = R.layout.fragment_currency_info
    override var mPresenter: AddToWatchlistContract.Presenter? = null

    override fun initView(rootView: View) {
    }
}