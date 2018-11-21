package com.makeuseof.cryptocurrency.domain.usecases.postlist

import com.makeuseof.utils.coroutine.model.Result
import com.makeuseof.cryptocurrency.data.post.model.PublisherPost

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
interface PostUseCases {
    suspend fun getPosts(): Result<List<PublisherPost>>?

    suspend fun getPost(id: Int): PublisherPost?
}