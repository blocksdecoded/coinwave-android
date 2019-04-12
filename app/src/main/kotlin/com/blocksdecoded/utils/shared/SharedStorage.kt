package com.blocksdecoded.utils.shared

import android.content.Context
import android.content.SharedPreferences
import com.blocksdecoded.utils.logE

@Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
class SharedStorage(
    context: Context,
    private var sharedFileName: String = "shared_prefs"
) : ISharedStorage {
    private var mPreferences: SharedPreferences = getPreferences(context)

    //region Private

    private fun getPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(
            sharedFileName,
            Context.MODE_PRIVATE
        )

    private fun editPreference(body: (SharedPreferences.Editor) -> Unit) {
        mPreferences.edit().apply {
            try {
                body.invoke(this)
            } catch (e: Exception) {
                logE(e)
            }
        }.apply()
    }

    //endregion

    //region Contract

    override fun <T> getPreference(key: String, defValue: T): T {
        mPreferences.let {
            return when (defValue) {
                is String -> { it.getString(key, defValue as String) }
                is Float -> { it.getFloat(key, defValue as Float) }
                is Int -> { it.getInt(key, defValue as Int) }
                is Boolean -> { it.getBoolean(key, defValue as Boolean) }
                is Long -> { it.getLong(key, defValue as Long) }
                is Set<*> -> { it.getStringSet(key, defValue as Set<String>) }

                else -> throw ClassNotFoundException()
            } as T
        }
    }

    override fun <T> setPreference(key: String, value: T) {
        editPreference {
            when (value) {
                is String -> { it.putString(key, value as String) }
                is Float -> { it.putFloat(key, value as Float) }
                is Int -> { it.putInt(key, value as Int) }
                is Boolean -> { it.putBoolean(key, value as Boolean) }
                is Long -> { it.putLong(key, value as Long) }
                is Set<*> -> { it.putStringSet(key, value as Set<String>) }
            }
        }
    }

    override fun containsPreference(key: String): Boolean = mPreferences.contains(key)

    override fun removePreference(key: String) = editPreference { it.remove(key) }

    //endregion
}