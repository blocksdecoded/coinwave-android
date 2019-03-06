package com.blocksdecoded.core.mvp

import androidx.annotation.CallSuper
import com.blocksdecoded.utils.Lg
import com.blocksdecoded.utils.coroutine.AppExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

abstract class BaseMVPPresenter<T>(
    var mView: T?
) : BaseMVPContract.Presenter<T> {

    protected val coroutineSupervisor = SupervisorJob()
    protected val scope = CoroutineScope(AppExecutors.main + coroutineSupervisor)

    init {
        injectSelfToView()
    }

    fun injectSelfToView() {
        try {
            @Suppress("LeakingThis")
            if (mView != null && mView is BaseMVPContract.View<*>) {
                @Suppress("LeakingThis")
                (mView as BaseMVPContract.View<BaseMVPContract.Presenter<*>>).setPresenter(this)
            }
        } catch (e: Exception) {
            Lg.d(e.message)
        }
    }

    @CallSuper
    override fun detachView() {
        try {
            if (mView != null && mView is BaseMVPContract.View<*>) {
                (mView as BaseMVPContract.View<BaseMVPContract.Presenter<*>>).clearPresenter()
            }
        } catch (e: Exception) {
            Lg.d(e.message)
        }
        mView = null
    }

    override fun onStart() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    @CallSuper
    override fun onDestroy() {
        mView = null
        coroutineSupervisor.cancel()
    }
}