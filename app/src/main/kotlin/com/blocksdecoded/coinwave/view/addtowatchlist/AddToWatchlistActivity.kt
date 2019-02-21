package com.blocksdecoded.coinwave.view.addtowatchlist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blocksdecoded.coinwave.domain.UseCaseProvider
import com.blocksdecoded.core.SwipeableActivity
import com.blocksdecoded.utils.inRightTransition
import com.blocksdecoded.utils.outRightTransition

/**
 * Created by askar on 2/11/19
 * with Android Studio
 */
class AddToWatchlistActivity : SwipeableActivity() {

    private var mPresenter: AddToWatchlistContract.Presenter? = null

    override fun onBackPressed() {
        super.onBackPressed()
        outRightTransition()
    }

    override fun finish() {
        super.finish()
        outRightTransition()
    }

    //region Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val fragment = AddToWatchlistFragment.newInstance()

            supportFragmentManager
                    .beginTransaction()
                    .add(android.R.id.content, fragment)
                    .commit()

            mPresenter = AddToWatchlistPresenter(
                    fragment,
                    UseCaseProvider.getCurrencyListUseCases(applicationContext)
            )
        }
    }

    //endregion

    companion object {
        fun start(context: Context) {
            context.startActivity(intent(context))

            if (context is Activity) {
                context.inRightTransition()
            }
        }

        fun intent(context: Context): Intent = Intent(context, AddToWatchlistActivity::class.java)
    }
}