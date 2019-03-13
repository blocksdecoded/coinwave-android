package com.blocksdecoded.coinwave.presentation.post

import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import com.blocksdecoded.core.mvp.BaseMvpContract

interface IPostContract {

    interface View : BaseMvpContract.View<Presenter> {
        fun showPost(post: PublisherPost)
    }

    interface Presenter : BaseMvpContract.Presenter<View> {
        fun getPost(id: Int)
    }
}