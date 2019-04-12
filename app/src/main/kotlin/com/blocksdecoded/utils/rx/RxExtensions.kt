package com.blocksdecoded.utils.rx

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable

fun <T> Observable<T>.observeUi() = this.subscribeOn(RxSchedulers.io)
    .observeOn(RxSchedulers.main)

fun <T> Observable<T>.observeIo() = this.subscribeOn(RxSchedulers.io)
    .observeOn(RxSchedulers.io)

fun <T> Single<T>.observeUi(): Single<T> = this.subscribeOn(RxSchedulers.io)
    .observeOn(RxSchedulers.main)

fun <T> Single<T>.observeIo(): Single<T> = this.subscribeOn(RxSchedulers.io)
    .observeOn(RxSchedulers.io)

fun <T> Single<T>.uiSubscribe(onNext: (T) -> Unit): Disposable = uiSubscribe(onNext, null)

fun <T> Observable<T>.uiSubscribe(onSuccess: (T) -> Unit): Disposable =
    uiSubscribe(onSuccess, null, null)

fun <T> Observable<T>.uiSubscribe(
    onNext: (T) -> Unit,
    onError: ((Throwable) -> Unit)? = null,
    onComplete: (() -> Unit)? = null
): Disposable =
    observeUi().subscribe(
        onNext,
        { throwable -> onError?.apply { this(throwable) } },
        { onComplete?.apply { this() } }
    )

fun <T> Single<T>.uiSubscribe(
    onSuccess: (T) -> Unit,
    onError: ((Throwable) -> Unit)? = null
): Disposable =
    observeUi().subscribe(
        onSuccess,
        { throwable -> onError?.apply { this(throwable) } }
    )
