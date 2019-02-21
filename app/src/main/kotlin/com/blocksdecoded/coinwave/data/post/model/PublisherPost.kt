package com.blocksdecoded.coinwave.data.post.model

import com.google.gson.annotations.SerializedName

import java.text.SimpleDateFormat

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */

data class PublisherPost(
    @SerializedName("ID") var id: Int,
    @SerializedName("post_date") var date: String?,
    @SerializedName("post_title") var title: String?,
    @SerializedName("author") var author: String?,
    @SerializedName("html") var html: String?,
    @SerializedName("url") var url: String?,
    @SerializedName("featured_image") var image: PublisherImage?
) {

    companion object {
        private val date_format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }
}
