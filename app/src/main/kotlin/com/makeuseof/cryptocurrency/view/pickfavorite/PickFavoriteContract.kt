package com.makeuseof.cryptocurrency.view.pickfavorite

import com.makeuseof.core.mvp.BaseMVPContract

interface PickFavoriteContract {

    interface View : BaseMVPContract.View<Presenter> {

    }

    interface Presenter : BaseMVPContract.Presenter<View> {

    }
}