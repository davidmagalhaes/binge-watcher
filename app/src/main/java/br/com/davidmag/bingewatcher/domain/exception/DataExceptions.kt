package br.com.davidmag.bingewatcher.domain.exception

import java.lang.Exception

data class EntityNotFoundException(
    override val message: String?,
    override val cause: Throwable? = null
) : Exception()