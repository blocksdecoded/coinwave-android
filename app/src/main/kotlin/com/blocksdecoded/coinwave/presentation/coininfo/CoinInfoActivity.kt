package com.blocksdecoded.coinwave.presentation.coininfo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blocksdecoded.core.SwipeableActivity
import com.blocksdecoded.utils.extensions.inRightTransition
import com.blocksdecoded.utils.extensions.outRightTransition

// Created by askar on 7/24/18.
class CoinInfoActivity : SwipeableActivity() {

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(android.R.id.content, CoinInfoFragment.newInstance())
                    .commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        outRightTransition()
    }

    override fun finish() {
        super.finish()
        outRightTransition()
    }

    companion object {
        private const val EXTRA_COIN_ID = "coin_id"

        fun start(context: Context, clientId: Int) {
            context.startActivity(intent(context, clientId))

            if (context is Activity) {
                context.inRightTransition()
            }
        }

        fun intent(context: Context, clientId: Int): Intent {
            val intent = Intent(context, CoinInfoActivity::class.java)

            intent.putExtra(EXTRA_COIN_ID, clientId)

            return intent
        }

        fun getIdFromIntent(intent: Intent): Int = intent.getIntExtra(EXTRA_COIN_ID, -1)
    }
}