package com.blocksdecoded.coinwave.presentation.post

import com.blocksdecoded.core.mvp.BaseMVPContract
import com.blocksdecoded.coinwave.data.post.model.PublisherPost

interface PostContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun showPost(post: PublisherPost)
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun getPost(id: Int)
    }
}