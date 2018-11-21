package com.makeuseof.core.model

// Created by askar on 6/18/18.
sealed class Result<out T : Any> {
    class Success<out T : Any>(val data: T) : Result<T>()

    class Error(val exception: Throwable?) : Result<Nothing>()
}