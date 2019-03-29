package com.blocksdecoded.coinwave.data.post

import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import io.reactivex.Observable

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
interface IPostStorage {
    fun getPosts(date: String): Observable<List<PublisherPost>>

    fun getPost(id: Int): PublisherPost?
}