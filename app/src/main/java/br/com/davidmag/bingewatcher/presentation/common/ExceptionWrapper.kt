package br.com.davidmag.bingewatcher.presentation.common

data class ExceptionWrapper(
	override val viewType : Int = PresentationObject.VIEWTYPE_ERROR,
	override val exception: Throwable?,
	val errorMessage : Int? = null,
	val errorArgs : List<*> = emptyList<Any>()
) : PresentationObject