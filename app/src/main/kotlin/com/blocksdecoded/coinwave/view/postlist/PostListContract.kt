package com.blocksdecoded.coinwave.view.postlist

import com.blocksdecoded.core.mvp.BaseMVPContract
import com.blocksdecoded.coinwave.data.post.model.PublisherPost

interface PostListContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun openPost(url: String)

        fun showLoading()

        fun stopLoading()

        fun showLoadingError()

        fun showErrorMessage()

        fun showPosts(posts: List<PublisherPost>)

        fun nextPosts(posts: List<PublisherPost>)
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun onPostClick(id: Int)

        fun getPosts()

        fun getNextPosts()

        fun onMenuClick()
    }
}