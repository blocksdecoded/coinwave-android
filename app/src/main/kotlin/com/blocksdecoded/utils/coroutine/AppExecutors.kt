package com.blocksdecoded.utils.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlin.coroutines.CoroutineContext

const val THREAD_COUNT = 3

/**
 * Global executor pools for the whole application.
 *
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
@ObsoleteCoroutinesApi
open class AppExecutors constructor(
        val ioContext: CoroutineContext = Dispatchers.Default,
        val networkContext: CoroutineContext = newFixedThreadPoolContext(THREAD_COUNT, "networkIO"),
        val uiContext: CoroutineContext = Dispatchers.Main){
    companion object {
        private var INSTANCE: AppExecutors? = null

        fun getInstance(): AppExecutors{
            if (INSTANCE == null) INSTANCE = AppExecutors()
            return INSTANCE!!
        }
    }
}