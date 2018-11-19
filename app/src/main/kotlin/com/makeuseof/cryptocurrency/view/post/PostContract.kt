package com.makeuseof.cryptocurrency.view.post

import com.makeuseof.core.mvp.BaseMVPContract

interface PostContract {

    interface View : BaseMVPContract.View<Presenter> {

    }

    interface Presenter : BaseMVPContract.Presenter<View> {

    }
}