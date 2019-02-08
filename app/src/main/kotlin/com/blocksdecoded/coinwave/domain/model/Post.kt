package com.blocksdecoded.coinwave.domain.model

import java.util.*

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
data class Post(
        val id: Int,
        var date: Date,
        var title: String? = null,
        var author: String? = null,
        var html: String? = null,
        var url: String? = null,
        var image: PostImage? = null
) {
}