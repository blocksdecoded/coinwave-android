package com.blocksdecoded.coinwave.data.post.remote

import com.blocksdecoded.coinwave.data.post.model.PostResponse
import io.reactivex.Single

interface IPostClient {
    suspend fun getPosts(date: String): Single<PostResponse>
}