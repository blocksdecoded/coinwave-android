package com.blocksdecoded.coinwave.view.addtowatchlist

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.blocksdecoded.core.SwipeableActivity
import com.blocksdecoded.utils.inRightTransition
import com.blocksdecoded.utils.outRightTransition

/**
 * Created by askar on 2/11/19
 * with Android Studio
 */
class AddToWatchlistActivity: SwipeableActivity() {

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

            if(context is Activity){
                context.inRightTransition()
            }
        }

        fun intent(context: Context): Intent = Intent(context, AddToWatchlistActivity::class.java)
    }
}