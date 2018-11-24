package com.makeuseof.cryptocurrency.view.pickfavorite

import android.annotation.SuppressLint
import android.os.Bundle
import com.makeuseof.core.SwipeableActivity
import com.makeuseof.utils.outRightTransition

/**
 * Created by askar on 11/24/18
 * with Android Studio
 */
class PickFavoriteActivity: SwipeableActivity() {
    private var mPresenter: PickFavoriteContract.Presenter? = null

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
                    fragment
            )
        }
    }
}