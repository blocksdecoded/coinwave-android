package com.blocksdecoded.coinwave.data.bootstrap

import com.blocksdecoded.coinwave.data.coins.remote.ICoinClientConfig
import com.blocksdecoded.utils.shared.ISharedStorage

class CoinClientConfig(
    private val sharedStorage: ISharedStorage
) : ICoinClientConfig {
    override var ipfsUrl: String
        get() = sharedStorage.getPreference(PREF_LAST_IPFS_URL, "")
        set(value) = sharedStorage.setPreference(PREF_LAST_IPFS_URL, value)

    companion object {
        private const val PREF_LAST_IPFS_URL = "ipfs_url"
    }
}