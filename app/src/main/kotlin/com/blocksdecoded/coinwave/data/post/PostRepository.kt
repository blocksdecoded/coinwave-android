package com.blocksdecoded.coinwave.data.post

import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import com.blocksdecoded.utils.coroutine.model.mapOnSuccess

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
class PostRepository(
    private val mLocal: IPostStorage?,
    private val mRemote: IPostStorage?
) : IPostStorage {
    private val mCache = HashMap<Int, PublisherPost>()

    //region Contract

    override suspend fun getPosts(date: String): Result<List<PublisherPost>>? =
            mRemote?.getPosts(date)
                    ?.mapOnSuccess {
                        it.forEach { mCache[it.id] = it }
                        it
                    }

    override fun getPost(id: Int): PublisherPost? = mCache[id]

    //endregion
}