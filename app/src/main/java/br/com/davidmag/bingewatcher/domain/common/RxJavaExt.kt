package br.com.davidmag.bingewatcher.domain.common

import io.reactivex.Maybe

/** Consumes the exception and calls the mapper passed as argument,
 * that can either return a value or throw another exception */
fun <T> Maybe<T>.onErrorMap(mapper: (exc: Throwable) -> T) : Maybe<T> {
    return onErrorResumeNext { e : Throwable ->
        Maybe.fromCallable {
            mapper(e)
        }
    }
}

fun <T> Maybe<T>.doBefore(func: () -> Unit) : Maybe<T> {
    return Maybe.fromCallable { func() }.flatMap { this }
}