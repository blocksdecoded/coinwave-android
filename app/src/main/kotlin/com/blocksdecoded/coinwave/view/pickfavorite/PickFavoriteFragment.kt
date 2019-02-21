package com.blocksdecoded.coinwave.view.pickfavorite

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import butterknife.BindView
import butterknife.OnClick
import com.blocksdecoded.core.mvp.BaseMVPFragment
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.model.CurrencyEntity
import com.blocksdecoded.coinwave.view.currencylist.recycler.CurrencyListAdapter
import com.blocksdecoded.coinwave.view.currencylist.recycler.CurrencyListViewHolder

class PickFavoriteFragment :
        BaseMVPFragment<PickFavoriteContract.Presenter>(),
        PickFavoriteContract.View,
        CurrencyListViewHolder.CurrencyVHClickListener {
    companion object {
        fun newInstance(): PickFavoriteFragment = PickFavoriteFragment()
    }

    override var mPresenter: PickFavoriteContract.Presenter? = null
    override val layoutId: Int = R.layout.fragment_pick_favorite

    @BindView(R.id.fragment_pick_favorite_recycler)
    @JvmField var mRecycler: RecyclerView? = null
    private var mAdapter: CurrencyListAdapter? = null

    @OnClick(R.id.back)
    fun onClick(view: View) {
        when (view.id) {
            R.id.back -> finishView()
        }
    }

    //region Lifecycle

    override fun initView(rootView: View) {
        mAdapter = CurrencyListAdapter(arrayListOf(), this)

        val lm = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mRecycler?.layoutManager = lm
        mRecycler?.adapter = mAdapter
    }

    //endregion

    //region Click

    override fun onClick(position: Int) {
        mPresenter?.onCurrencyClick(position)
    }

    override fun onPick(position: Int) {
    }

    //endregion

    //region Contract

    override fun showCurrencies(currencies: List<CurrencyEntity>) {
        mAdapter?.setItems(currencies)
    }

    //endregion
}