package com.blocksdecoded.utils.rx

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

object RxSchedulers {
    val io: Scheduler = Schedulers.io()
    val mainThread = AndroidSchedulers.mainThread()
    val computation = Schedulers.computation()
}