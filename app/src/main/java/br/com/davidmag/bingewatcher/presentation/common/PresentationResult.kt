package br.com.davidmag.bingewatcher.presentation.common

sealed class PresentationResult<T>(
    open val data: T? = null
) {
    data class ResultSuccess<T>(
        override val data: T
    ): PresentationResult<T>(data)

    data class ResultError<T>(
        val error: Throwable,
        override val data: T? = null
    ): PresentationResult<T>()

    class ResultLoading<T>: PresentationResult<T>()
    class ResultEmpty<T>: PresentationResult<T>()
}