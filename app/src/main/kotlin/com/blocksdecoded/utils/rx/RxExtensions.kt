package com.blocksdecoded.utils.rx

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.Disposable

fun <T> Single<T>.uiSubscribe(onNext: (T) -> Unit): Disposable = uiSubscribe(onNext)

fun <T> Flowable<T>.uiSubscribe(onNext: (T) -> Unit): Disposable =
    uiSubscribe(onNext, null, null)

fun <T> Flowable<T>.uiSubscribe(
    onNext: (T) -> Unit,
    onError: ((Throwable) -> Unit)? = null,
    onComplete: (() -> Unit)? = null
): Disposable =
    subscribeOn(RxSchedulers.io)
        .observeOn(RxSchedulers.mainThread)
        .subscribe(
            onNext,
            { throwable -> onError?.apply { this(throwable) } },
            { onComplete?.apply { this() } }
        )

fun <T> Single<T>.uiSubscribe(
    onNext: (T) -> Unit,
    onError: ((Throwable) -> Unit)? = null
): Disposable =
    subscribeOn(RxSchedulers.io)
        .observeOn(RxSchedulers.mainThread)
        .subscribe(
            onNext,
            { throwable -> onError?.apply { this(throwable) } }
        )
