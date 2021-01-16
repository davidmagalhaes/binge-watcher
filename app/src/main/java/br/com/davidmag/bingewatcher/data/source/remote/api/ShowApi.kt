package br.com.davidmag.bingewatcher.data.source.remote.api

import br.com.davidmag.bingewatcher.data.source.remote.dto.ShowResponse
import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Query

interface ShowApi {

    @GET("shows")
    fun list(@Query("page") page: Int) : Maybe<@JvmSuppressWildcards List<ShowResponse>>

    @GET("lookup/shows")
    fun lookup(@Query("imdb") imdb : String) : Maybe<@JvmSuppressWildcards ShowResponse>

    @GET("search/shows")
    fun search(@Query("q") query : String) : Maybe<@JvmSuppressWildcards List<ShowResponse>>

}