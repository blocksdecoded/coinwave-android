package com.blocksdecoded.utils.rx

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable

fun <T> Observable<T>.observeUI() = this.subscribeOn(RxSchedulers.io)
    .observeOn(RxSchedulers.mainThread)

fun <T> Observable<T>.observeIO() = this.subscribeOn(RxSchedulers.io)
    .observeOn(RxSchedulers.io)

fun <T> Single<T>.observeUI(): Single<T> = this.subscribeOn(RxSchedulers.io)
    .observeOn(RxSchedulers.mainThread)

fun <T> Single<T>.observeIO(): Single<T> = this.subscribeOn(RxSchedulers.io)
    .observeOn(RxSchedulers.io)

fun <T> Single<T>.uiSubscribe(onNext: (T) -> Unit): Disposable = uiSubscribe(onNext)

fun <T> Observable<T>.uiSubscribe(onNext: (T) -> Unit): Disposable =
    uiSubscribe(onNext, null, null)

fun <T> Observable<T>.uiSubscribe(
    onNext: (T) -> Unit,
    onError: ((Throwable) -> Unit)? = null,
    onComplete: (() -> Unit)? = null
): Disposable =
    observeUI().subscribe(
        onNext,
        { throwable -> onError?.apply { this(throwable) } },
        { onComplete?.apply { this() } }
    )

fun <T> Single<T>.uiSubscribe(
    onNext: (T) -> Unit,
    onError: ((Throwable) -> Unit)? = null
): Disposable =
    observeUI().subscribe(
        onNext,
        { throwable -> onError?.apply { this(throwable) } }
    )
