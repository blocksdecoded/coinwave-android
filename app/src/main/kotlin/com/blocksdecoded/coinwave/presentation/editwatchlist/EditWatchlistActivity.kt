package com.blocksdecoded.coinwave.presentation.editwatchlist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blocksdecoded.core.SwipeableActivity
import com.blocksdecoded.utils.extensions.inRightTransition
import com.blocksdecoded.utils.extensions.outRightTransition

/**
 * Created by askar on 2/11/19
 * with Android Studio
 */
class EditWatchlistActivity : SwipeableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(android.R.id.content, EditWatchlistFragment.newInstance())
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
        fun start(context: Context) {
            context.startActivity(intent(context))

            if (context is Activity) {
                context.inRightTransition()
            }
        }

        fun intent(context: Context): Intent = Intent(context, EditWatchlistActivity::class.java)
    }
}