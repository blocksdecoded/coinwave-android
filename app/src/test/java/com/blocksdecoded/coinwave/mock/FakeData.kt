package com.blocksdecoded.coinwave.mock

import com.blocksdecoded.coinwave.data.post.model.PublisherPost

object FakeData {
    val postsResponse
            get() = listOf(
                PublisherPost(1, "1234213", "", "", "", "", null)
            )
}