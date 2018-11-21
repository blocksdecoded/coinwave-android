package com.makeuseof.cryptocurrency.data.post

import com.makeuseof.core.model.Result
import com.makeuseof.cryptocurrency.data.post.model.PublisherPost

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
interface PostDataSource {
    suspend fun getPosts(date: String): Result<List<PublisherPost>>?

    fun getPost(id: Int): PublisherPost?
}