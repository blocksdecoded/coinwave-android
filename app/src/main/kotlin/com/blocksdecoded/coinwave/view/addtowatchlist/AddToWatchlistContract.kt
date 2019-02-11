package com.blocksdecoded.coinwave.view.addtowatchlist

import com.blocksdecoded.core.mvp.BaseMVPContract

interface AddToWatchlistContract {

    interface View : BaseMVPContract.View<Presenter> {

    }

    interface Presenter : BaseMVPContract.Presenter<View> {

    }
}