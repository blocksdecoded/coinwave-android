package com.blocksdecoded.coinwave.domain.usecases.postlist

import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.coinwave.data.post.PostDataSource
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import com.blocksdecoded.utils.coroutine.AppExecutors
import com.blocksdecoded.utils.coroutine.model.onSuccess
import kotlinx.coroutines.withContext

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
class PostInteractor(
        private val appExecutors: AppExecutors,
        private val mPostsSource: PostDataSource
): PostUseCases {
    private var date = ""

    private fun updateLastDate(posts: List<PublisherPost>){
        date = if (posts.isNotEmpty()) {
            posts.last().date?:""
        } else {
            ""
        }
    }

    override suspend fun getPosts(): Result<List<PublisherPost>>? = withContext(appExecutors.networkContext) {
        date = ""
        mPostsSource.getPosts(date)?.onSuccess {
            updateLastDate(it)
        }
    }

    override suspend fun getNextPosts(): Result<List<PublisherPost>>? = withContext(appExecutors.networkContext) {
        mPostsSource.getPosts(date)?.onSuccess {
            updateLastDate(it)
        }
    }

    override suspend fun getPost(id: Int): PublisherPost? {
        return mPostsSource.getPost(id)
    }
}