package com.makeuseof.cryptocurrency.view.currency

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.makeuseof.core.SwipeableActivity
import com.makeuseof.core.network.NetworkErrorHandler
import com.makeuseof.cryptocurrency.domain.UseCaseProvider
import com.makeuseof.utils.inRightTransition
import com.makeuseof.utils.outRightTransition

// Created by askar on 7/24/18.
class CurrencyActivity: SwipeableActivity() {
    companion object {
        private val CURRENCY_ID_FIELD = "currency_id"

        fun start(context: Context, clientId: Int) {
            context.startActivity(intent(context, clientId))

            if(context is Activity){
                context.inRightTransition()
            }
        }

        fun intent(context: Context, clientId: Int): Intent {
            val intent = Intent(context, CurrencyActivity::class.java)

            intent.putExtra(CURRENCY_ID_FIELD, clientId)

            return intent
        }

        fun getIdFromIntent(intent: Intent): Int = intent.getIntExtra(CURRENCY_ID_FIELD, -1)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        outRightTransition()
    }

    override fun finish() {
        super.finish()
        outRightTransition()
    }

    private var mPresenter: CurrencyContract.Presenter? = null

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkErrorHandler.getInstance(applicationContext)

        if (savedInstanceState == null) {
            val fragment = CurrencyFragment()

            supportFragmentManager
                    .beginTransaction()
                    .add(android.R.id.content, fragment)
                    .commit()

            mPresenter = CurrencyPresenter(
                    fragment,
                    UseCaseProvider.getChartsInteractor(this),
                    UseCaseProvider.getCurrencyListUseCases(this)
            )

            mPresenter?.fetchCurrencyData(getIdFromIntent(intent))
        }
    }
}