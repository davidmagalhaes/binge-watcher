package br.com.davidmag.bingewatcher.data.source.remote.api

import br.com.davidmag.bingewatcher.data.source.remote.dto.SearchResponse
import br.com.davidmag.bingewatcher.data.source.remote.dto.SeasonResponse
import br.com.davidmag.bingewatcher.data.source.remote.dto.ShowImageDto
import br.com.davidmag.bingewatcher.data.source.remote.dto.ShowResponse
import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ShowApi {

    @GET("shows")
    fun fetch(@Query("page") page: Int) : Maybe<@JvmSuppressWildcards List<ShowResponse>>

    @GET("shows/{id}")
    fun lookup(@Path("id") showId : Long) : Maybe<@JvmSuppressWildcards ShowResponse>

    @GET("search/shows")
    fun search(@Query("q") query : String) : Maybe<@JvmSuppressWildcards List<SearchResponse>>

    @GET("shows/{id}/seasons")
    fun seasons(@Path("id") showId : Long) : Maybe<@JvmSuppressWildcards List<SeasonResponse>>

    @GET("shows/{id}/images")
    fun fetchImages(@Path("id") showId : Long) : Maybe<@JvmSuppressWildcards List<ShowImageDto>>
}