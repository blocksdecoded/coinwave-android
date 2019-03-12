package com.blocksdecoded.core.mvp

import androidx.annotation.CallSuper
import com.blocksdecoded.utils.coroutine.AppExecutors
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

abstract class BaseMvpPresenter<T> : BaseMvpContract.Presenter<T> {

    protected val disposables: CompositeDisposable = CompositeDisposable()
    protected val coroutineSupervisor = SupervisorJob()
    protected val scope = CoroutineScope(AppExecutors.main + coroutineSupervisor)

    override fun onStart() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    @CallSuper
    override fun onDestroy() {
        view = null
        coroutineSupervisor.cancel()
        disposables.clear()
    }
}