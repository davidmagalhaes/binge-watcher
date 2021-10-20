package br.com.davidmag.bingewatcher.presentation.model

import br.com.davidmag.bingewatcher.presentation.common.PresentationObject

data class ShowAggregatePresentation (
	override val viewType: Int,
	val show : ShowPresentation? = null,
	val favored : Boolean? = null,
	val episodes : List<EpisodePresentation> = emptyList(),
	val subtitle : String? = null,
	val seasonTitles : List<String> = emptyList()
) : PresentationObject