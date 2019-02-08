package com.blocksdecoded.coinwave.view.pickfavorite

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blocksdecoded.core.SwipeableActivity
import com.blocksdecoded.coinwave.domain.UseCaseProvider
import com.blocksdecoded.utils.inRightTransition
import com.blocksdecoded.utils.outRightTransition

/**
 * Created by askar on 11/24/18
 * with Android Studio
 */
class PickFavoriteActivity: SwipeableActivity() {
    private var mPresenter: PickFavoriteContract.Presenter? = null

    companion object {
        fun start(context: Context) {
            context.startActivity(intent(context))

            if(context is Activity){
                context.inRightTransition()
            }
        }

        fun intent(context: Context): Intent {
            val intent = Intent(context, PickFavoriteActivity::class.java)

            return intent
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

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val fragment = PickFavoriteFragment.newInstance()

            supportFragmentManager
                    .beginTransaction()
                    .add(android.R.id.content, fragment)
                    .commit()

            mPresenter = PickFavoritePresenter(
                    fragment,
                    UseCaseProvider.getFavoriteUseCases(applicationContext),
                    UseCaseProvider.getCurrencyListUseCases(applicationContext)
            )
        }
    }
}