package com.makeuseof.cryptocurrency.util

import android.widget.TextView
import java.util.*

fun TextView?.setRegularFont(){
	this?.let {
		typeface = OpenSansTypeface.getRegular(it.context)
	}
}

fun TextView?.setSemiboldFont(){
	this?.let {
		typeface = OpenSansTypeface.getSemibold(it.context)
	}
}

fun TextView?.setBoldFont(){
	this?.let {
		typeface = OpenSansTypeface.getBold(it.context)
	}
}

fun TextView?.setHeavyFont(){
	this?.let {
		typeface = SFUITypeface.getHeavy(it.context)
	}
}

fun TextView?.setMediumFont(){
	this?.let {
		typeface = OpenSansTypeface.getRegular(it.context)
	}
}

fun TextView?.setThinFont(){
	this?.let {
		typeface = OpenSansTypeface.getLight(it.context)
	}
}

fun TextView?.setLightFont(){
	this?.let {
		typeface = OpenSansTypeface.getLight(it.context)
	}
}

object TextUtils {
	fun localeToEmoji(locale: Locale): String {
		val countryCode = locale.country
		val firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6
		val secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6
		return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
	}

    fun codeToEmoji(code: String): String{
        val flagOffset = 0x1F1E6
        val asciiOffset = 0x41

        val firstChar = Character.codePointAt(code, 0) - asciiOffset + flagOffset
        val secondChar = Character.codePointAt(code, 1) - asciiOffset + flagOffset

        return String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
    }
}