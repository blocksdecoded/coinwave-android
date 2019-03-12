package com.blocksdecoded.coinwave.data.post.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.text.SimpleDateFormat

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */

data class PublisherPost(
    @Expose @SerializedName("ID") var id: Int,
    @Expose @SerializedName("post_date") var date: String?,
    @Expose @SerializedName("post_title") var title: String?,
    @Expose @SerializedName("author") var author: String?,
    @Expose @SerializedName("html") var html: String?,
    @Expose @SerializedName("url") var url: String?,
    @Expose @SerializedName("featured_image") var image: PublisherImage?
) {

    override fun toString(): String = "$id $title $url $image"

    companion object {
        private val date_format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }
}
