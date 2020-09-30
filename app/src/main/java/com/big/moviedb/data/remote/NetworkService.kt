package com.big.moviedb.data.remote

import com.big.moviedb.data.remote.response.MovieResults
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface NetworkService {

    //https://api.themoviedb.org/3/search/movie?api_key=f7fcb1af0e061120f757e91afba158c6&query=batman&page=1
    @GET(Endpoints.ENDPOINT_SEARCH_API)
    fun searchImages(
            @Query(Endpoints.QUERY_API_KEY) apiKey: String,
            @Query(Endpoints.QUERY_QUERY) querySearch: String,
            @Query(Endpoints.QUERY_PAGE) pageNumber: Int
    ): Call<MovieResults>

}