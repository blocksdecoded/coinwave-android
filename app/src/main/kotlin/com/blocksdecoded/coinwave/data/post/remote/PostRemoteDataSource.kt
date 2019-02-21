package com.blocksdecoded.coinwave.data.post.remote

import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.utils.coroutine.model.mapOnSuccess
import com.blocksdecoded.coinwave.data.post.PostDataSource
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import com.blocksdecoded.utils.retrofit.BaseRetrofitDataSource

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
class PostRemoteDataSource : BaseRetrofitDataSource(), PostDataSource {

    companion object {
        private var INSTANCE: PostDataSource? = null

        fun getInstance(): PostDataSource {
            if (INSTANCE == null)
                INSTANCE = PostRemoteDataSource()
            return INSTANCE!!
        }
    }

    private val mClient: PostNetworkClient = getRetrofitClient(
            PostConfig.BASE_URL,
            PostNetworkClient::class.java
    )

    //region Contract

    override suspend fun getPosts(date: String): Result<List<PublisherPost>> {
        val options = HashMap<String, String>()

        if (date.isNotEmpty()) {
            options["last_item_datetime"] = date
        }

        return mClient.getPosts(options)
                .getResult()
                .mapOnSuccess { it.posts }
    }

    override fun getPost(id: Int): PublisherPost? = null

    //endregion
}