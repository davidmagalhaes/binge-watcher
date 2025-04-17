package br.com.davidmag.bingewatcher.presentation.model

import br.com.davidmag.bingewatcher.presentation.common.PresentationResult

data class ShowAggregatePresentation (
	val show : ShowPresentation? = null,
	val favored : Boolean? = null,
	val episodes : List<EpisodePresentation> = emptyList(),
	val subtitle : String? = null,
	val seasonTitles : List<String> = emptyList()
)