package br.com.davidmag.bingewatcher.data.source.remote.api

import br.com.davidmag.bingewatcher.data.source.remote.dto.EpisodeResponse
import br.com.davidmag.bingewatcher.data.source.remote.dto.ShowResponse
import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EpisodeApi {

    @GET("shows/{id}/episodes")
    fun list(@Path("id") showId : Long) : Maybe<@JvmSuppressWildcards List<EpisodeResponse>>

    @GET("shows/{id}/episodebynumber")
    fun lookup(
        @Path("id") showId : Long,
        @Query("season") season : Int,
        @Query("number") number : Int
    ) : Maybe<@JvmSuppressWildcards ShowResponse>
}