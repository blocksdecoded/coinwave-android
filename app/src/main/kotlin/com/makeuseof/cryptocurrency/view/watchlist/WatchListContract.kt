package com.makeuseof.cryptocurrency.view.watchlist

import com.makeuseof.core.mvp.BaseMVPContract

interface WatchListContract {

    interface View : BaseMVPContract.View<Presenter> {

    }

    interface Presenter : BaseMVPContract.Presenter<View> {

    }
}