package com.makeuseof.cryptocurrency.data.post.remote

import com.makeuseof.utils.coroutine.model.Result
import com.makeuseof.utils.coroutine.model.mapOnSuccess
import com.makeuseof.cryptocurrency.data.post.PostDataSource
import com.makeuseof.cryptocurrency.data.post.model.PublisherPost
import com.makeuseof.utils.retrofit.BaseRetrofitDataSource

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
class PostRemoteDataSource: BaseRetrofitDataSource(), PostDataSource {

    companion object {
        private var INSTANCE: PostDataSource? = null

        fun getInstance(): PostDataSource{
            if (INSTANCE == null)
                INSTANCE = PostRemoteDataSource()
            return INSTANCE!!
        }
    }

    private val mClient: PostNetworkClient = getRetrofit(PostConfig.BASE_URL)
            .create(PostNetworkClient::class.java)

    //region Contract

    override suspend fun getPosts(date: String): Result<List<PublisherPost>> {
        val options = HashMap<String, String>()

//        options["cats"] = "19"

        if (date.isNotEmpty()) {
            options["last_item_datetime"] = date
        }

        val call = mClient.getPosts(options)

        return call.getResult().mapOnSuccess { postResponse ->
            postResponse.posts
        }
    }

    override fun getPost(id: Int): PublisherPost? {
        return null
    }

    //endregion
}