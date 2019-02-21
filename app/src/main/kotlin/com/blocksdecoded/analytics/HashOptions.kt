package com.blocksdecoded.analytics

/**
 * Created by Tameki on 2/8/18.
 */
class HashOptions<T, V> : HashMap<T, V>() {
    fun putOption(key: T, value: V): HashOptions<T, V> {
        put(key, value)

        return this
    }
}