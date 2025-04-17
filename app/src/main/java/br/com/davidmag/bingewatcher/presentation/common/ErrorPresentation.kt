package br.com.davidmag.bingewatcher.presentation.common

data class ErrorPresentation(
	val type: Int = 0,
	val message : Int? = null,
	val args : List<*> = emptyList<Any>()
)