package com.blocksdecoded.coinwave.domain.usecases.posts

import com.blocksdecoded.coinwave.data.post.IPostStorage
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import io.reactivex.Observable

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
class PostsInteractor(
    private val mPostsSource: IPostStorage
) : IPostsUseCases {
    var date = ""

    private fun updateLastDate(posts: List<PublisherPost>) {
        date = if (posts.isNotEmpty()) {
            posts.last().date ?: ""
        } else {
            ""
        }
    }

    override suspend fun getPosts(): Observable<List<PublisherPost>> {
        date = ""
        return mPostsSource.getPosts(date)
            .map {
                updateLastDate(it)
                it
            }
    }

    override suspend fun getNextPosts(): Observable<List<PublisherPost>> = mPostsSource.getPosts(date).map {
        updateLastDate(it)
        it
    }

    override fun getPost(id: Int): PublisherPost? = mPostsSource.getPost(id)
}