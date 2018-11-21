package com.makeuseof.cryptocurrency.domain.usecases.postlist

import com.makeuseof.core.model.Result
import com.makeuseof.core.model.Result.Success
import com.makeuseof.cryptocurrency.data.post.PostDataSource
import com.makeuseof.cryptocurrency.data.post.model.PublisherPost
import com.makeuseof.utils.coroutine.AppExecutors
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

    override suspend fun getPosts(): Result<List<PublisherPost>>? = withContext(appExecutors.networkContext) {
        val result = mPostsSource.getPosts(date)

        when(result) {
            is Success -> { date = result.data.last().date?:"" }
        }

        result
    }

    override suspend fun getPost(id: Int): PublisherPost? {
        return mPostsSource.getPost(id)
    }
}