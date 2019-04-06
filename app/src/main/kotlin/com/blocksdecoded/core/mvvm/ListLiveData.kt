package com.blocksdecoded.core.mvvm

import androidx.lifecycle.MutableLiveData

class ListLiveData<T> : MutableLiveData<ArrayList<T>>() {
    fun addAll(items: List<T>?) {
        if (value != null && items != null) {
            value?.addAll(items)
            value = value
        }
    }

    fun clear() {
        if (value != null) {
            value?.clear()
            value = value
        }
    }
}