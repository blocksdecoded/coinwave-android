package com.blocksdecoded.coinwave.data.post.remote

import com.blocksdecoded.coinwave.data.post.model.PostResponse
import com.blocksdecoded.utils.coroutine.model.Result

interface IPostClient {
    suspend fun getPosts(date: String): Result<PostResponse>
}