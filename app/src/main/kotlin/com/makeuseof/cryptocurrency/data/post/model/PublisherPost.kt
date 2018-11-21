package com.makeuseof.cryptocurrency.data.post.model

import com.squareup.moshi.Json

import java.text.SimpleDateFormat

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */

data class PublisherPost(
        @Json(name = "ID") var id: Int,
        @Json(name = "post_date") var date: String?,
        @Json(name = "post_title") var title: String?,
        @Json(name = "author") var author: String?,
        @Json(name = "html") var html: String?,
        @Json(name = "url") var url: String?,
        @Json(name = "featured_image") var image: PublisherImage?
) {

    companion object {
        private val date_format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }
}
