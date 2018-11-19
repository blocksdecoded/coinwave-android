package com.makeuseof.cryptocurrency.view.postlist

import com.makeuseof.core.mvp.BaseMVPContract

interface PostListContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun openPost(postId: Int)
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun onPostClick(position: Int)


    }
}