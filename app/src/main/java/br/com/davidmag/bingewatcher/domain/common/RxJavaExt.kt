package br.com.davidmag.bingewatcher.domain.common

import io.reactivex.Maybe

fun <T> Maybe<T>.onErrorRethrow(mapper: (exc: Throwable) -> Throwable) : Maybe<T> {
    return onErrorResumeNext { e : Throwable ->
        Maybe.fromCallable {
            throw mapper(e)
        }
    }
}