package com.blocksdecoded.coinwave.data.crypto.remote

interface ICoinClientConfig {
    var coinUrl: String
    val ipnsKey: String
        get() = coinUrl
            .substringAfter("ipns", "QmT3qT84rdZ9tkUssYLNkKbPcFTkutFy2S8uizvKWQbTMg")
            .replace("/", "")
}