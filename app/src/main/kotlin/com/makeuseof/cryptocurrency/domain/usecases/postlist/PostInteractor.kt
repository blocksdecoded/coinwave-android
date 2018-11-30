package com.makeuseof.cryptocurrency.domain.usecases.postlist

import com.makeuseof.utils.coroutine.model.Result
import com.makeuseof.utils.coroutine.model.Result.Success
import com.makeuseof.cryptocurrency.data.post.PostDataSource
import com.makeuseof.cryptocurrency.data.post.model.PublisherPost
import com.makeuseof.utils.coroutine.AppExecutors
import com.makeuseof.utils.coroutine.model.onSuccess
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