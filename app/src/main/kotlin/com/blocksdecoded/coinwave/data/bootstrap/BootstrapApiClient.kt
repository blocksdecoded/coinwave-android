package com.blocksdecoded.coinwave.data.bootstrap

import io.reactivex.Single

class BootstrapApiClient : IBootstrapClient {
    override fun getConfigs(): Single<BootstrapResponse> = Single.just(BootstrapResponse())
}