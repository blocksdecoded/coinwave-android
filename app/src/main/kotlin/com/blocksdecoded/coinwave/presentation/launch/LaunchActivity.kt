package com.blocksdecoded.coinwave.presentation.launch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blocksdecoded.coinwave.data.bootstrap.IBootstrapClient
import com.blocksdecoded.coinwave.data.crypto.remote.ICoinClientConfig
import com.blocksdecoded.coinwave.presentation.main.MainActivity
import com.blocksdecoded.utils.logE
import com.blocksdecoded.utils.rx.uiSubscribe
import org.koin.android.ext.android.inject

class LaunchActivity : AppCompatActivity() {

    private val bootstrapClient: IBootstrapClient by inject()
    private val coinClientConfig: ICoinClientConfig by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchConfigs()
    }

    private fun fetchConfigs() {
        bootstrapClient.getConfigs()
            .uiSubscribe(
                onNext = {
                    if (it.servers.isNotEmpty()) coinClientConfig.coinUrl = it.servers.first()
                    startMain()
                },
                onError = {
                    logE(Exception(it))
                    startMain()
                }
            )
    }

    private fun startMain() {
        MainActivity.start(this)
        finish()
    }
}