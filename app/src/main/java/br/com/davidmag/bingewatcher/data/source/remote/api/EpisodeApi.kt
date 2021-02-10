package br.com.davidmag.bingewatcher.data.source.remote.api

import br.com.davidmag.bingewatcher.data.source.remote.dto.EpisodeResponse
import br.com.davidmag.bingewatcher.data.source.remote.dto.ShowResponse
import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EpisodeApi {
    @GET("seasons/{seasonId}/episodes")
    fun fetch(@Path("seasonId") seasonId : Long) : Maybe<@JvmSuppressWildcards List<EpisodeResponse>>
}