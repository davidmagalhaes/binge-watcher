package br.com.davidmag.bingewatcher.presentation.common

class PresentationWrapper<T>(
    override val viewType: Int,
    val value : T? = null
) : PresentationObject