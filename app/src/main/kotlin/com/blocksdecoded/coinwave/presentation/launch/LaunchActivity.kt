package com.blocksdecoded.coinwave.presentation.launch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blocksdecoded.coinwave.data.bootstrap.IBootstrapClient
import com.blocksdecoded.coinwave.presentation.main.MainActivity
import com.blocksdecoded.utils.logD
import com.blocksdecoded.utils.rx.uiSubscribe
import org.koin.android.ext.android.inject

class LaunchActivity: AppCompatActivity() {

    val bootstrapClient: IBootstrapClient by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchConfigs()
    }

    private fun fetchConfigs() {
        bootstrapClient.getConfigs()
            .uiSubscribe(
                onNext = {
                    logD("Bootstrap response $it")
                    //TODO: Set configs
                    startMain()
                },
                onError = {
                    logD("Bootstrap error $it")
                    startMain()
                }
            )
    }

    private fun startMain() {
        MainActivity.start(this)
        finish()
    }
}