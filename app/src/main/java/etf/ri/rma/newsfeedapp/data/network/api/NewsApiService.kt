package etf.ri.rma.newsfeedapp.data.network.api

import etf.ri.rma.newsfeedapp.dto.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApiService {

    @GET("news/top")
    suspend fun getTopStoriesByCategory(
        @Query("api_token") apiToken: String,
        @Query("categories") category: String,
        @Query("language") language: String
    ): NewsResponse

    @GET("news/similar/{uuid}")
    suspend fun getSimilarNews(
        @Path("uuid") uuid: String,
        @Query("api_token") apiToken: String,
    ): NewsResponse


}