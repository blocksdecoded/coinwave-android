package com.blocksdecoded.coinwave.data.post.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */

data class PublisherImage(
    @Expose @SerializedName("featured") var featured: String?,
    @Expose @SerializedName("thumb") var thumb: String?,
    @Expose @SerializedName("middle") var middle: String?
)
