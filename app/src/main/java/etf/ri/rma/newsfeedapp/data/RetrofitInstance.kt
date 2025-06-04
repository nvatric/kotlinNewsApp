package etf.ri.rma.newsfeedapp.data

import com.google.gson.GsonBuilder
import etf.ri.rma.newsfeedapp.data.network.api.ImagaApiService
import etf.ri.rma.newsfeedapp.data.network.api.NewsApiService
import etf.ri.rma.newsfeedapp.dto.Source
import etf.ri.rma.newsfeedapp.dto.SourceDeserializer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val gson = GsonBuilder()
        .registerTypeAdapter(Source::class.java, SourceDeserializer())
        .create()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thenewsapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    val api: NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java)
    }


    val imaggaApi: ImagaApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.imagga.com/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImagaApiService::class.java)
    }
}
