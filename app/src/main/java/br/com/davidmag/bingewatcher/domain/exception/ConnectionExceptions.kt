package br.com.davidmag.bingewatcher.domain.exception

import java.io.IOException

class ConnectionException(
    cause: Throwable,
    message: String? = null
): IOException(message, cause)