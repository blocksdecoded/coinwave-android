package com.blocksdecoded.utils.shared

import android.content.Context
import android.content.SharedPreferences

// Created by askar on 5/31/18.
@Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
class SharedStorage(
    context: Context,
    private var mSharedFileName: String = "shared_prefs"
) : ISharedStorage {

    companion object {
        private var INSTANCE: ISharedStorage? = null

        fun getInstance(context: Context): ISharedStorage {
            if (INSTANCE == null) {
                INSTANCE = SharedStorage(context)
            }

            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

    private var mSharedPreferences: SharedPreferences? = null

    init {
        mSharedPreferences = getPreferences(context)
    }

    //region Private

    private fun getPreferences(context: Context): SharedPreferences =
            context.getSharedPreferences(
                    mSharedFileName,
                    Context.MODE_PRIVATE
            )

    private fun editPreference(body: (SharedPreferences.Editor) -> Unit) {
        mSharedPreferences?.edit()?.apply {
            try {
                body.invoke(this)
            } catch (e: Exception) {
            }
        }?.apply()
    }

    //endregion

    //region Contract

    override fun containsPreference(key: String): Boolean {
        mSharedPreferences?.contains(key)?.let { return it }
        return false
    }

    override fun <T> setPreference(key: String, value: T) {
        editPreference {
            when (value) {
                is String -> { it.putString(key, value as String) }
                is Float -> { it.putFloat(key, value as Float) }
                is Int -> { it.putInt(key, value as Int) }
                is Boolean -> { it.putBoolean(key, value as Boolean) }
                is Set<*> -> { it.putStringSet(key, value as Set<String>) }
                is Long -> { it.putLong(key, value as Long) }
            }
        }
    }

    override fun <T> getPreference(key: String, defValue: T): T {
        try {
            mSharedPreferences?.let {
                return when (defValue) {
                    is String -> { it.getString(key, defValue as String) }
                    is Float -> { it.getFloat(key, defValue as Float) }
                    is Int -> { it.getInt(key, defValue as Int) }
                    is Boolean -> { it.getBoolean(key, defValue as Boolean) }
                    is Set<*> -> { it.getStringSet(key, defValue as Set<String>) }
                    is Long -> { it.getLong(key, defValue as Long) }

                    else -> defValue
                } as T
            }
        } catch (e: Exception) {
        }

        return defValue
    }

    override fun removePreference(key: String) = if (containsPreference(key)) {
        editPreference { it.remove(key) }
    } else {}

    //endregion
}