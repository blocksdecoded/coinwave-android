package com.makeuseof.cryptocurrency.view.postlist

import com.makeuseof.core.mvp.BaseMVPContract

interface PostListContract {

    interface View : BaseMVPContract.View<Presenter> {

    }

    interface Presenter : BaseMVPContract.Presenter<View> {

    }
}