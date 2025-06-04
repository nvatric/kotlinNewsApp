package etf.ri.rma.newsfeedapp.data.network.api

import etf.ri.rma.newsfeedapp.data.APIToken
import etf.ri.rma.newsfeedapp.dto.ImaggaResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ImagaApiService {
    @GET("v2/tags")
    suspend fun getTags(
        @Query("image_url") imageUrl: String,
        @Header("Authorization") auth: String = APIToken.getImaggaToken()
    ): ImaggaResponse
}