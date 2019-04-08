package com.blocksdecoded.core.mvvm

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class CoreViewModel : ViewModel() {
    private val disposables = CompositeDisposable()

    fun Disposable.addDisposable() {
        disposables.add(this)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}