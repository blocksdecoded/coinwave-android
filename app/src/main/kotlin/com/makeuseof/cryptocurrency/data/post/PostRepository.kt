package com.makeuseof.cryptocurrency.data.post

import com.makeuseof.utils.coroutine.model.Result
import com.makeuseof.cryptocurrency.data.post.model.PublisherPost

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
class PostRepository(
        private val mLocal: PostDataSource?,
        private val mRemote: PostDataSource?
): PostDataSource {
    companion object {
        private var INSTANCE: PostDataSource? = null

        fun getInstance(
                local: PostDataSource?,
                remote: PostDataSource?
        ): PostDataSource{
            if (INSTANCE == null)
                INSTANCE = PostRepository(local, remote)
            return INSTANCE!!
        }
    }

    private val mCache = HashMap<Int, PublisherPost>()

    //region Contract

    override suspend fun getPosts(date: String): Result<List<PublisherPost>>? {
        val result = mRemote?.getPosts(date)

        when(result) {
            is Result.Success -> {
                result.data.forEach { mCache[it.id] = it }
            }
        }

        return result
    }

    override fun getPost(id: Int): PublisherPost? = mCache[id]

    //endregion
}