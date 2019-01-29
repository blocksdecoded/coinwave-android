package com.makeuseof.cryptocurrency.data.post.model

import com.google.gson.annotations.SerializedName

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */

data class PublisherImage(
        @SerializedName("featured") var featured: String?,
        @SerializedName("thumb") var thumb: String?,
        @SerializedName("middle") var middle: String?
)
