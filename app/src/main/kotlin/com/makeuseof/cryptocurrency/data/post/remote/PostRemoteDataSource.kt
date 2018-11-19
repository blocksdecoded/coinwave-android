package com.makeuseof.cryptocurrency.data.post.remote

import com.makeuseof.cryptocurrency.data.post.PostSourceContract

/**
 * Created by askar on 11/19/18
 * with Android Studio
 */
class PostRemoteDataSource: PostSourceContract {
    companion object {
        private var INSTANCE: PostSourceContract? = null

        fun getInstance(): PostSourceContract{
            if (INSTANCE == null)
                INSTANCE = PostRemoteDataSource()
            return INSTANCE!!
        }
    }
}