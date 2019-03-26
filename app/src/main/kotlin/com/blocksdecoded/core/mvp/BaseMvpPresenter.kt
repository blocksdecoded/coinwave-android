package com.blocksdecoded.core.mvp

import androidx.annotation.CallSuper
import com.blocksdecoded.utils.coroutine.AppExecutors
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

abstract class BaseMvpPresenter<T> : BaseMvpContract.Presenter<T> {

    protected val disposables: CompositeDisposable = CompositeDisposable()
    protected val coroutineSupervisor = SupervisorJob()
    protected val scope = CoroutineScope(AppExecutors.main + coroutineSupervisor)

    protected fun Disposable?.addDisposable() {
        this?.let { disposables.add(it) }
    }

    override fun attachView(view: T) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

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