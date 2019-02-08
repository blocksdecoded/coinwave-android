package com.blocksdecoded.coinwave.data.post.remote

import com.blocksdecoded.coinwave.data.post.model.PostResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
interface PostNetworkClient {
    @GET(PostConfig.POSTS_URL)
    fun getPosts(@QueryMap options: Map<String, String>): Call<PostResponse>
}