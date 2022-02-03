package br.com.davidmag.bingewatcher.domain.common

import com.google.gson.Gson
import timber.log.Timber

fun Timber.Forest.i(logParams: LogParams, tag: String) {
    synchronized(this) {
        tag(tag)
        i(Gson().toJson(logParams))
    }
}

fun Timber.Forest.i(logParams: LogParams) {
    i(Gson().toJson(logParams))
}
