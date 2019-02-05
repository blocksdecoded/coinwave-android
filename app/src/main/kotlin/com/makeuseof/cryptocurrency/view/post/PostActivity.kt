package com.makeuseof.cryptocurrency.view.post

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.makeuseof.core.SwipeableActivity
import com.makeuseof.cryptocurrency.domain.UseCaseProvider
import com.makeuseof.utils.inRightTransition
import com.makeuseof.utils.outRightTransition

/**
 * Created by askar on 11/22/18
 * with Android Studio
 */

class PostActivity: SwipeableActivity(){
    companion object {
        private val POST_ID_FIELD = "currency_id"

        fun start(context: Context, postId: Int) {
            context.startActivity(intent(context, postId))

            if(context is Activity){
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

            mPresenter = PostPresenter(
                    fragment,
                    UseCaseProvider.getPostUseCases()
            )

            mPresenter?.getPost(getIdFromIntent(intent))
        }
    }
}