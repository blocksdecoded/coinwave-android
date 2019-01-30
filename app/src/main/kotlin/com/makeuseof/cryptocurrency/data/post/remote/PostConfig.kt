package com.makeuseof.cryptocurrency.data.post.remote

import com.makeuseof.cryptocurrency.BuildConfig

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
object PostConfig {
    const val BASE_URL = BuildConfig.API_POSTS
    const val PUBLISHER_URL = "/blocksdecoded"
    const val POSTS_URL = "$PUBLISHER_URL/posts"
}