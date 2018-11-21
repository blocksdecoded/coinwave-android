package com.makeuseof.cryptocurrency.data.post.model

import com.squareup.moshi.Json

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
data class PostResponse(
        @Json(name="posts") val posts: List<PublisherPost>
)