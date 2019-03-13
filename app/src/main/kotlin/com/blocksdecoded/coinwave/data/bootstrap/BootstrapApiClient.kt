package com.blocksdecoded.coinwave.data.bootstrap

import com.blocksdecoded.core.network.CoreApiClient
import io.reactivex.Single
import retrofit2.http.GET

class BootstrapApiClient : CoreApiClient(), IBootstrapClient {

    private val mClient: BootstrapNetworkClient

    init {
        mClient = getRetrofitClient(
            BootstrapNetworkClient.BASE_URL,
            BootstrapNetworkClient::class.java
        )
    }

    override fun getConfigs(): Single<BootstrapResponse> = mClient.getBootstrap()

    private interface BootstrapNetworkClient {
        @GET("/bootstrap.json")
        fun getBootstrap(): Single<BootstrapResponse>

        companion object {
            const val BASE_URL = "http://fridayte.ch"
        }
    }
}