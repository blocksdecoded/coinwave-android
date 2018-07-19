package com.makeuseof.cryptocurrency.view.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.makeuseof.core.model.Result
import com.makeuseof.cryptocurrency.R
import com.makeuseof.cryptocurrency.data.crypto.CryptoService
import com.makeuseof.cryptocurrency.domain.usecases.list.CryptoListInteractor
import com.makeuseof.utils.Lg
import com.makeuseof.utils.coroutine.AppExecutors
import com.makeuseof.utils.coroutine.launchSilent
import kotlinx.coroutines.experimental.android.UI

class MainActivity : AppCompatActivity() {

    val interactor = CryptoListInteractor(AppExecutors(), CryptoService.getInstance())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    private fun fetchData() = launchSilent(UI){
        val result = interactor.getCryptoList()
        when(result){
            is Result.Success -> {
                result.data.forEach {
                    Lg.d(it.toString())
                }
            }

            is Result.Error -> {
                Lg.d(result.exception.message)
            }
        }
    }
}
