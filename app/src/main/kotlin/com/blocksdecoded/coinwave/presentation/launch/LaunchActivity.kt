package com.blocksdecoded.coinwave.presentation.launch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.bootstrap.IBootstrapClient
import com.blocksdecoded.coinwave.data.coins.remote.ICoinClientConfig
import com.blocksdecoded.coinwave.presentation.main.MainActivity
import com.blocksdecoded.utils.extensions.hide
import com.blocksdecoded.utils.extensions.visible
import com.blocksdecoded.utils.logD
import com.blocksdecoded.utils.logE
import com.blocksdecoded.utils.rx.uiSubscribe
import kotlinx.android.synthetic.main.activity_launch_screen.*
import kotlinx.android.synthetic.main.partial_watchlist_error.*
import org.koin.android.ext.android.inject

class LaunchActivity : AppCompatActivity() {

    private val bootstrapClient: IBootstrapClient by inject()
    private val coinClientConfig: ICoinClientConfig by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_screen)
        fetchConfigs()

        connection_error_retry.setOnClickListener {
            fetchConfigs()
        }
    }

    private fun fetchConfigs() {
        launch_screen_container.hide()
        bootstrapClient.getConfigs()
            .uiSubscribe(
                onSuccess = {
                    if (it.servers.isNotEmpty()) coinClientConfig.ipfsUrl = it.servers[0]
                    startMain()
                },
                onError = {
                    logE(Exception(it))

                    if (coinClientConfig.ipfsUrl.isEmpty()) {
                        showConnectionError()
                    } else {
                        startMain()
                    }
                }
            )
    }

    private fun showConnectionError() {
        launch_screen_container.visible()
    }

    private fun startMain() {
        MainActivity.start(this)
        finish()
    }
}