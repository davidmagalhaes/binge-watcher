package br.com.davidmag.bingewatcher.presentation.common

data class ExceptionWrapper(
	override val viewType : Int,
	override val exception: Throwable?,
	val errorMessage : Int? = null,
	val errorArgs : List<Any> = emptyList()
) : PresentationObject