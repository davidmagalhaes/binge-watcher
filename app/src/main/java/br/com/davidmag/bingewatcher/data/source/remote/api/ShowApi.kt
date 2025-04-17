package br.com.davidmag.bingewatcher.data.source.remote.api

import br.com.davidmag.bingewatcher.data.source.remote.dto.SearchResponse
import br.com.davidmag.bingewatcher.data.source.remote.dto.SeasonResponse
import br.com.davidmag.bingewatcher.data.source.remote.dto.ShowImageDto
import br.com.davidmag.bingewatcher.data.source.remote.dto.ShowResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ShowApi {

    @GET("shows")
    suspend fun fetch(@Query("page") page: Int) : List<ShowResponse>

    @GET("shows/{id}")
    suspend fun lookup(@Path("id") showId : Long) : ShowResponse

    @GET("search/shows")
    suspend fun search(
        @Query("q") query : String,
        @Query("page") page: Int
    ) : List<SearchResponse>

    @GET("shows/{id}/seasons")
    suspend fun seasons(@Path("id") showId : Long) : List<SeasonResponse>

    @GET("shows/{id}/images")
    suspend fun fetchImages(@Path("id") showId : Long) : List<ShowImageDto>
}