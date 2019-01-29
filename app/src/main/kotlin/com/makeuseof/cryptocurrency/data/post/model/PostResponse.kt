package com.makeuseof.cryptocurrency.data.post.model

import com.google.gson.annotations.SerializedName

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
data class PostResponse(
        @SerializedName("posts") val posts: List<PublisherPost>
)