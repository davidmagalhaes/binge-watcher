package br.com.davidmag.bingewatcher.domain.aggr

import br.com.davidmag.bingewatcher.domain.model.Episode
import br.com.davidmag.bingewatcher.domain.model.Show

data class ShowAggregate (
    val show : Show,
    val episodes : List<Episode>
)