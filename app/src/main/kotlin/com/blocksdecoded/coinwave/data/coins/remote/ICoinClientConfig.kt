package com.blocksdecoded.coinwave.data.coins.remote

import okhttp3.HttpUrl

interface ICoinClientConfig {
    var ipfsUrl: String

    val ipnsPath: String
        get() = HttpUrl.get(ipfsUrl).encodedPath()
}