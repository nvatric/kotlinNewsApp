package etf.ri.rma.newsfeedapp.data.network

import etf.ri.rma.newsfeedapp.data.RetrofitInstance
import etf.ri.rma.newsfeedapp.data.network.api.ImagaApiService
import etf.ri.rma.newsfeedapp.data.network.exception.InvalidImageURLException
import java.net.URL

class ImagaDAO {
    private var apiService: ImagaApiService = RetrofitInstance.imaggaApi

    fun setApiService(service: ImagaApiService) {
        apiService = service
    }

    private fun isValidUrl(url: String): Boolean {
        return try {
            URL(url)
            true
        } catch (e: Exception) {
            false
        }
    }

    private val tagCache = mutableMapOf<String, List<String>>()

    suspend fun getTags(imageUrl: String): List<String> {
        if (!isValidUrl(imageUrl)) throw InvalidImageURLException("Invalid image URL: $imageUrl")

        tagCache[imageUrl]?.let { return it }

        val response = apiService.getTags(imageUrl)

        tagCache[imageUrl] = response.tags
        return response.tags
    }
}


