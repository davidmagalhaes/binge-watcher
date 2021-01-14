package br.com.davidmag.bingewatcher.data.source.remote.api

import br.com.davidmag.bingewatcher.data.source.remote.dto.ShowResponse
import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Query

interface ShowApi {

    @GET("shows")
    fun listShows(@Query("page") page: Int) : Maybe<@JvmSuppressWildcards List<ShowResponse>>

}