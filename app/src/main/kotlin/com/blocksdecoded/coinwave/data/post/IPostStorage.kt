package com.blocksdecoded.coinwave.data.post

import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.coinwave.data.post.model.PublisherPost

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
interface IPostStorage {
    suspend fun getPosts(date: String): Result<List<PublisherPost>>?

    fun getPost(id: Int): PublisherPost?
}