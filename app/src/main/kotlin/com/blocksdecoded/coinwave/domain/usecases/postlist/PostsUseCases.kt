package com.blocksdecoded.coinwave.domain.usecases.postlist

import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.coinwave.data.post.model.PublisherPost

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
interface PostsUseCases {
    suspend fun getPosts(): Result<List<PublisherPost>>?

    suspend fun getNextPosts(): Result<List<PublisherPost>>?

    suspend fun getPost(id: Int): PublisherPost?
}