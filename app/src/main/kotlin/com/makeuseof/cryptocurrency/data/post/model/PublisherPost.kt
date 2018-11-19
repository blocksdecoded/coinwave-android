package com.makeuseof.cryptocurrency.data.post.model

import com.squareup.moshi.Json

import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by abaikirov on 10/3/17.
 */

data class PublisherPost(
        @Json(name = "ID") var id: Int = 0,
        @Json(name = "post_date") var date: String? = null,
        @Json(name = "post_title") var title: String? = null,
        @Json(name = "author") var author: String? = null,
        @Json(name = "html") var html: String? = null,
        @Json(name = "url") var url: String? = null,
        @Json(name = "featured_image") var image: PublisherImage? = null
) {

    companion object {
        private val date_format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }
}
