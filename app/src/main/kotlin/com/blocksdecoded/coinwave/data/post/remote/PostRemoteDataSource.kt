package com.blocksdecoded.coinwave.data.post.remote

import com.blocksdecoded.coinwave.BuildConfig
import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.utils.coroutine.model.mapOnSuccess
import com.blocksdecoded.coinwave.data.post.PostDataSource
import com.blocksdecoded.coinwave.data.post.model.PostResponse
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import com.blocksdecoded.core.network.CoreApiClient
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
object PostRemoteDataSource : CoreApiClient(), PostDataSource {

    private val mClient: PostNetworkClient = getRetrofitClient(
            PostNetworkClient.BASE_URL,
            PostNetworkClient::class.java
    )

    //region Contract

    override suspend fun getPosts(date: String): Result<List<PublisherPost>> {
        val options = HashMap<String, String>()

        if (date.isNotEmpty()) {
            options["last_item_datetime"] = date
        }

        return mClient.getPosts(options).getResult().mapOnSuccess { it.posts }
    }

    override fun getPost(id: Int): PublisherPost? = null

    //endregion

    private interface PostNetworkClient {

        @GET(POSTS_URL)
        fun getPosts(@QueryMap options: Map<String, String>): Call<PostResponse>

        companion object {
            const val BASE_URL = BuildConfig.API_POSTS
            const val PUBLISHER_URL = "/blocksdecoded"
            const val POSTS_URL = "$PUBLISHER_URL/posts"
        }
    }
}