package com.blocksdecoded.utils

// Created by askar on 6/21/18.
fun String?.exactEmpty(): Boolean{
    var trim = this?.replace(" ", "")
    trim = trim?.replace("\n", "")
    trim = trim?.replace("\t", "")
    trim = trim?.replace("\r", "")

    if (trim.isNullOrEmpty()) return true

    if (trim == "null") return true

    return false
}