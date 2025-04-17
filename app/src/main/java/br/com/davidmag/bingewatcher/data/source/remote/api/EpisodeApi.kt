package br.com.davidmag.bingewatcher.data.source.remote.api

import br.com.davidmag.bingewatcher.data.source.remote.dto.EpisodeResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

interface EpisodeApi {
    @GET("seasons/{seasonId}/episodes")
    suspend fun fetch(@Path("seasonId") seasonId : Long) : List<EpisodeResponse>
}