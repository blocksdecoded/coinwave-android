package com.makeuseof.cryptocurrency.view.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makeuseof.core.mvp.BaseMVPFragment

class WatchListFragment : BaseMVPFragment<WatchListContract.Presenter>(), WatchListContract.View {
    override var mPresenter: WatchListContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}