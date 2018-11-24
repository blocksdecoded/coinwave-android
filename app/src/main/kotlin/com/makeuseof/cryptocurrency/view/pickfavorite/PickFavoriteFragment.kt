package com.makeuseof.cryptocurrency.view.pickfavorite

import android.view.View
import com.makeuseof.core.mvp.BaseMVPFragment
import com.makeuseof.cryptocurrency.R

class PickFavoriteFragment : BaseMVPFragment<PickFavoriteContract.Presenter>(), PickFavoriteContract.View {
    companion object {
        fun newInstance(): PickFavoriteFragment = PickFavoriteFragment()
    }

    override var mPresenter: PickFavoriteContract.Presenter? = null
    override val layoutId: Int = R.layout.fragment_currency_list

    override fun initView(rootView: View) {

    }
}