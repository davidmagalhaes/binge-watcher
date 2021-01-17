package br.com.davidmag.bingewatcher.presentation.model

import br.com.davidmag.bingewatcher.presentation.common.PresentationObject

data class ShowPresentation(
    override val viewType: Int,
    val id : Long
) : PresentationObject