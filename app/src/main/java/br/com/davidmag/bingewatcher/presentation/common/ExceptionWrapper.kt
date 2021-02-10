package br.com.davidmag.bingewatcher.presentation.common

data class ExceptionWrapper(
	override val exception: Throwable,
	override val viewType : Int = PresentationObject.VIEWTYPE_ERROR,
	val errorMessage : Int? = null,
	val errorArgs : List<*> = emptyList<Any>()
) : PresentationObject