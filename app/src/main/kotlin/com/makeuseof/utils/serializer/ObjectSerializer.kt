package com.makeuseof.utils.serializer

import android.util.Base64
import android.util.Base64.NO_WRAP
import android.util.Base64.NO_PADDING
import android.util.Base64OutputStream
import android.util.Base64InputStream
import java.io.*
import java.nio.charset.Charset


// Created by askar on 6/8/18.
class ObjectSerializer {
    fun convertToString(source: Serializable): String?{
        try {
            val baos = ByteArrayOutputStream()
            val oos = ObjectOutputStream(
                    Base64OutputStream(baos, Base64.NO_PADDING or Base64.NO_WRAP))
            oos.writeObject(source)
            oos.close()
            return baos.toString("UTF-8")
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    fun <T>convertToObject(source: String): T?{
        try {
            return ObjectInputStream(Base64InputStream(
                    ByteArrayInputStream(source.toByteArray(Charset.forName("UTF-8"))), NO_PADDING or NO_WRAP)
            ).readObject() as T
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}