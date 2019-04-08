package com.blocksdecoded.coinwave.data.coins.remote

import okhttp3.HttpUrl

interface ICoinClientConfig {
    var ipnsUrl: String

    val ipnsPath: String
        get() = HttpUrl.get(ipnsUrl).encodedPath()
}