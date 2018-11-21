package com.makeuseof.cryptocurrency.view.postlist

import com.makeuseof.core.mvp.BaseMVPContract
import com.makeuseof.cryptocurrency.data.post.model.PublisherPost

interface PostListContract {

    interface View : BaseMVPContract.View<Presenter> {
        fun openPost(postId: Int)

        fun showLoading()

        fun stopLoading()

        fun showPosts(posts: List<PublisherPost>)

        fun nextPosts(posts: List<PublisherPost>)
    }

    interface Presenter : BaseMVPContract.Presenter<View> {
        fun onPostClick(id: Int)

        fun getPosts()

        fun getNextPosts()
    }
}