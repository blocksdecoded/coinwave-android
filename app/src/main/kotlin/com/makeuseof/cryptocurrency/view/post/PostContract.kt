package com.makeuseof.cryptocurrency.view.post

import com.makeuseof.core.mvp.BaseMVPContract
import com.makeuseof.cryptocurrency.data.post.model.PublisherPost

interface PostContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun showPost(post: PublisherPost)
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun getPost(id: Int)
    }
}