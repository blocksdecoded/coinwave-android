package com.makeuseof.cryptocurrency.data.post.remote

import com.makeuseof.muocore.models.PublisherPost
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
interface PostNetworkClient {
    @GET("URL")
    fun getPosts(@Query("last_item_datetime")lastItem: String): List<PublisherPost>
}