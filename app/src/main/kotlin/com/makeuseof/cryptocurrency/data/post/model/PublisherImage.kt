package com.makeuseof.cryptocurrency.data.post.model

import com.squareup.moshi.Json

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */

data class PublisherImage(
        @Json(name = "featured") var featured: String?,
        @Json(name = "thumb") var thumb: String?,
        @Json(name = "middle") var middle: String?
)
