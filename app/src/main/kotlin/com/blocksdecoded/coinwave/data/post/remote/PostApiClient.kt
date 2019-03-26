package com.blocksdecoded.coinwave.data.post.remote

import com.blocksdecoded.coinwave.BuildConfig
import com.blocksdecoded.coinwave.data.post.model.PostResponse
import com.blocksdecoded.core.network.CoreApiClient
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
class PostApiClient : CoreApiClient(), IPostClient {

    private val mClient: PostNetworkClient = getRetrofitClient(
            PostNetworkClient.BASE_URL,
            PostNetworkClient::class.java
    )

    override suspend fun getPosts(date: String): Single<PostResponse> {
        val options = HashMap<String, String>()

        if (date.isNotEmpty()) {
            options["last_item_datetime"] = date
        }

        return mClient.getPosts(options)
    }

    private interface PostNetworkClient {

        @GET(POSTS_URL)
        fun getPosts(@QueryMap options: Map<String, String>): Single<PostResponse>

        companion object {
            const val BASE_URL = BuildConfig.API_POSTS
            const val PUBLISHER_URL = "/blocksdecoded"
            const val POSTS_URL = "$PUBLISHER_URL/posts"
        }
    }
}