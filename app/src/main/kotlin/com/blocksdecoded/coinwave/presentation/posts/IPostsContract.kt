package com.blocksdecoded.coinwave.presentation.posts

import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import com.blocksdecoded.core.mvp.BaseMvpContract

interface IPostsContract {

    interface View : BaseMvpContract.View<Presenter> {
        fun openPost(url: String)

        fun showLoading()

        fun stopLoading()

        fun showLoadingError()

        fun showErrorMessage()

        fun showPosts(posts: List<PublisherPost>)

        fun nextPosts(posts: List<PublisherPost>)
    }

    interface Presenter : BaseMvpContract.Presenter<View> {
        fun onPostClick(id: Int)

        fun getPosts()

        fun getNextPosts()

        fun onMenuClick()
    }
}