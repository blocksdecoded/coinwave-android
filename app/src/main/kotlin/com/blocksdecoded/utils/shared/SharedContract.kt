package com.blocksdecoded.utils.shared

// Created by askar on 5/31/18.
interface SharedContract {
    fun <T>setPreference(key: String, value: T)

    fun <T>getPreference(key: String, defValue: T): T

    fun removePreference(key: String)

    fun containsPreference(key: String): Boolean
}