package com.blocksdecoded.coinwave.data.bootstrap

import io.reactivex.Single

interface IBootstrapClient {
    fun getConfigs(): Single<BootstrapResponse>
}