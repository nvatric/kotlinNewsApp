package etf.ri.rma.newsfeedapp.data.network

import android.util.Log
import etf.ri.rma.newsfeedapp.data.APIToken
import etf.ri.rma.newsfeedapp.data.RetrofitInstance
import etf.ri.rma.newsfeedapp.data.SavedNewsDAO
import etf.ri.rma.newsfeedapp.data.network.api.NewsApiService
import etf.ri.rma.newsfeedapp.data.network.exception.InvalidUUIDException
import etf.ri.rma.newsfeedapp.dto.toNewsItem
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.model.toNews
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

class NewsDAO(private val savedNewsDAO: SavedNewsDAO) {

    private var apiService: NewsApiService = RetrofitInstance.api
    private val allStories = mutableListOf<NewsItem>()
    private val categoryCache = mutableMapOf<String, Pair<List<NewsItem>, Long>>()
    private val CACHE_DURATION = 30 * 1000L

    fun setApiService(api: NewsApiService) {
        this.apiService = api
    }

    fun getAllStories(): List<NewsItem> {
        return allStories.toList()
    }

    private fun isCacheValid(category: String): Boolean {
        val cached = categoryCache[category] ?: return false
        val now = System.currentTimeMillis()
        return (now - cached.second) <= CACHE_DURATION
    }

    suspend fun getTopStoriesByCategory(category: String): List<NewsItem> = withContext(Dispatchers.IO) {
        val validCategories = setOf(
            "general", "science", "sports", "business", "health",
            "entertainment", "technology", "politics", "tech", "food", "travel"
        )

        if (category !in validCategories) {
            throw IllegalArgumentException("Invalid category: $category")
        }

        val cached = categoryCache[category]
        if (cached != null && isCacheValid(category)) {
            return@withContext cached.first
        }

        try {
            val response = apiService.getTopStoriesByCategory(
                apiToken = APIToken.getToken(),
                category = category,
                language = "en"
            )

            val newStories = response.data.map { it.toNewsItem() }
                .filter { story -> allStories.none { it.uuid == story.uuid } }
                .take(3)

            newStories.forEach { story ->
                story.category = category
                story.isFeatured = true
                allStories.add(story)

                val added = savedNewsDAO.saveNews(story.toNews())
                if (added) {
                    val newsId = savedNewsDAO.getNewsIdByUUID(story.uuid)
                    if (newsId != null) {
                        savedNewsDAO.addTags(story.imageTags.map { it.value }, newsId)
                    }
                }


            }

            allStories.filterNot { newStories.contains(it) }.forEach {
                it.isFeatured = false
            }

            categoryCache[category] = Pair(newStories, System.currentTimeMillis())
            return@withContext newStories

        } catch (e: Exception) {
            Log.e("NewsDAO", "Greška prilikom dohvata vijesti: ${e.message}")
            throw Exception("Failed to fetch news: ${e.message}")
        }
    }

    suspend fun getSimilarStories(uuid: String): List<NewsItem> = withContext(Dispatchers.IO) {
        try {
            val uuidRegex = Pattern.compile("^[0-9a-fA-F-]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
            if (!uuidRegex.matcher(uuid).matches()) throw InvalidUUIDException("Invalid UUID format: $uuid")

            val response = apiService.getSimilarNews(
                uuid = uuid,
                apiToken = APIToken.getToken()
            )

            val result = response.data.map { it.toNewsItem() }.take(2)

            result.forEach { story ->
                if (allStories.none { it.uuid == story.uuid }) {
                    allStories.add(story)
                }

                val added = savedNewsDAO.saveNews(story.toNews())
                if (added) {
                    val newsId = savedNewsDAO.getNewsIdByUUID(story.uuid)
                    if (newsId != null) {
                        savedNewsDAO.addTags(story.imageTags.map { it.value }, newsId)
                    }
                }

            }

            return@withContext result

        } catch (e: InvalidUUIDException) {
            Log.e("NewsDAO", "Greška: ${e.message}")
            throw e
        } catch (e: Exception) {
            Log.e("NewsDAO", "Fallback: ${e.message}")
            val currentStory = allStories.find { it.uuid == uuid }
            return@withContext currentStory?.let {
                allStories.filter {
                    it.category == currentStory.category && it.uuid != uuid
                }.take(2)
            } ?: emptyList()
        }
    }
}
