package com.blocksdecoded.coinwave.data.config

import com.blocksdecoded.coinwave.data.crypto.remote.ICoinClientConfig
import com.blocksdecoded.coinwave.data.post.remote.IPostClientConfig

interface IConfigProvider : ICoinClientConfig, IPostClientConfig