package com.blocksdecoded.coinwave.data.config

data class ConfigProvider(
    override var coinUrl: String,
    override var postsUrl: String
) : IConfigProvider