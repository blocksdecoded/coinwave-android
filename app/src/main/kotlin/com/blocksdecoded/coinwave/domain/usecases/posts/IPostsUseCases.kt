package com.blocksdecoded.coinwave.domain.usecases.posts

import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import io.reactivex.Observable

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
interface IPostsUseCases {
    fun getPosts(): Observable<List<PublisherPost>>

    fun getNextPosts(): Observable<List<PublisherPost>>

    fun getPost(id: Int): PublisherPost?
}