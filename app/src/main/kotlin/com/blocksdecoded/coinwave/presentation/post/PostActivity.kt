package com.blocksdecoded.coinwave.presentation.post

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blocksdecoded.core.SwipeableActivity
import com.blocksdecoded.coinwave.domain.UseCaseProvider
import com.blocksdecoded.utils.extensions.inRightTransition
import com.blocksdecoded.utils.extensions.outRightTransition

/**
 * Created by askar on 11/22/18
 * with Android Studio
 */

class PostActivity : SwipeableActivity() {
    companion object {
        private val POST_ID_FIELD = "coin_id"

        fun start(context: Context, postId: Int) {
            context.startActivity(intent(context, postId))

            if (context is Activity) {
                context.inRightTransition()
            }
        }

        fun intent(context: Context, postId: Int): Intent {
            val intent = Intent(context, PostActivity::class.java)

            intent.putExtra(POST_ID_FIELD, postId)

            return intent
        }

        fun getIdFromIntent(intent: Intent): Int = intent.getIntExtra(POST_ID_FIELD, -1)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        outRightTransition()
    }

    override fun finish() {
        super.finish()
        outRightTransition()
    }

    private var mPresenter: PostContract.Presenter? = null

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val fragment = PostFragment.newInstance()

            supportFragmentManager
                    .beginTransaction()
                    .add(android.R.id.content, fragment)
                    .commit()

//            mPresenter = PostPresenter(
//                    fragment,
//                    UseCaseProvider.postUseCase
//            )
            //TODO: Initialize presenter

            mPresenter?.getPost(getIdFromIntent(intent))
        }
    }
}