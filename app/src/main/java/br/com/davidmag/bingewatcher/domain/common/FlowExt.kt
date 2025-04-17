package br.com.davidmag.bingewatcher.domain.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

/** Consumes the exception and calls the mapper passed as argument,
 * that can either return a value or throw another exception */
fun <T> Flow<T>.onErrorMap(mapper: (exc: Throwable) -> T) : Flow<T> {
    return catch { e -> emit(mapper(e)) }
}