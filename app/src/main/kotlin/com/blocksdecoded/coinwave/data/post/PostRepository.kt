package com.blocksdecoded.coinwave.data.post

import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import com.blocksdecoded.coinwave.data.post.remote.IPostClient
import io.reactivex.Observable

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
class PostRepository(
    private val mLocal: IPostStorage?,
    private val mRemote: IPostClient
) : IPostStorage {
    private val mCache = HashMap<Int, PublisherPost>()

    //region Contract

    override suspend fun getPosts(date: String): Observable<List<PublisherPost>> =
            mRemote.getPosts(date)
                .map {
                    it.posts.forEach { mCache[it.id] = it }
                    it.posts
                }
                .toObservable()

    override fun getPost(id: Int): PublisherPost? = mCache[id]

    //endregion
}