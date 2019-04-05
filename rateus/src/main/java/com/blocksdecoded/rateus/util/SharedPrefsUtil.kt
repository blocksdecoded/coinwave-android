package com.blocksdecoded.rateus.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Tameki on 3/20/18.
 */
internal object SharedPrefsUtil {
    private fun getPreferences(context: Context): SharedPreferences =
            context.getSharedPreferences(
                    "shared_app_rate",
                    Context.MODE_PRIVATE
            )

    fun <T> setPreference(context: Context, key: String, value: T) {
        val edit = getPreferences(context).edit()

        when (value) {
            is Int -> { edit.putInt(key, value) }
            is Boolean -> { edit.putBoolean(key, value) }
            is String -> { edit.putString(key, value) }
            is Float -> { edit.putFloat(key, value) }
        }

        edit.apply()
    }

    fun getBooleanPreference(context: Context, key: String): Boolean =
            getPreferences(context).getBoolean(key, true)

    fun getIntPreference(context: Context, key: String): Int = getPreferences(context).getInt(key, 0)

    fun getFloatPreference(context: Context, key: String): Float = getPreferences(context).getFloat(key, 0f)

    fun getStringPreference(context: Context, key: String): String =
            getPreferences(context).getString(key, "")?:""
}