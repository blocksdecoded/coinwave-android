package com.makeuseof.cryptocurrency.data.post

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
class PostRepository(
        private val mLocal: PostSourceContract?,
        private val mRemote: PostSourceContract?
): PostSourceContract {
    companion object {
        private var INSTANCE: PostSourceContract? = null

        fun getInstance(
                local: PostSourceContract,
                remote: PostSourceContract
        ): PostSourceContract{
            if (INSTANCE == null)
                INSTANCE = PostRepository(local, remote)
            return INSTANCE!!
        }
    }
}