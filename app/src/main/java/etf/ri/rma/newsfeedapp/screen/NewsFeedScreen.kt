package etf.ri.rma.newsfeedapp.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import etf.ri.rma.newsfeedapp.data.SavedNewsDAO
import etf.ri.rma.newsfeedapp.data.network.NewsDAO
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.model.toNewsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val categoryDisplayToApi = mapOf(
    "Politika" to "politics",
    "Sport" to "sports",
    "Nauka/tehnologija" to "tech",
    "Svijet" to "general",
    "All" to "general"
)

fun mapToApiCategory(displayCategory: String): String {
    return categoryDisplayToApi[displayCategory] ?: "general"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsFeedScreen(
    navController: NavController,
    newsDAO: NewsDAO,
    savedNewsDAO: SavedNewsDAO
) {
    var allNews by remember { mutableStateOf<List<NewsItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var selectedSort by rememberSaveable { mutableStateOf<SortOption?>(null) }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedRange by remember { mutableStateOf<Pair<String, String>?>(null) }
    var unwantedWords by remember { mutableStateOf(emptyList<String>()) }
    var lastGlobalCallTime by remember { mutableStateOf(0L) }

    val coroutineScope = rememberCoroutineScope()
    val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val navEntry = navController.currentBackStackEntryAsState().value

    fun loadNewsFromDatabase() {
        coroutineScope.launch(Dispatchers.IO) {
            val entities = savedNewsDAO.getAllNews()
            val items = entities.map {
                val tags = savedNewsDAO.getTags(it.id)
                it.toNewsItem(tags)
            }
            withContext(Dispatchers.Main) {
                allNews = items
            }
        }
    }

    fun fetchCategoryNews(displayCategory: String) {
        if (displayCategory == "All") {
            loadNewsFromDatabase()
            return
        }

        val apiCategory = mapToApiCategory(displayCategory)
        val currentTime = System.currentTimeMillis()
        val thirtySecondsInMillis = 30 * 1000

        if (currentTime - lastGlobalCallTime > thirtySecondsInMillis) {
            coroutineScope.launch {
                isLoading = true
                try {
                    newsDAO.getTopStoriesByCategory(apiCategory)
                    loadNewsFromDatabase()
                    lastGlobalCallTime = currentTime
                } catch (e: Exception) {
                    Log.e("NewsFeedScreen", "Error fetching category news", e)
                } finally {
                    isLoading = false
                }
            }
        }
    }

    LaunchedEffect(navEntry) {
        navEntry?.savedStateHandle?.get<Boolean>("refreshRequired")?.let { shouldRefresh ->
            if (shouldRefresh) {
                loadNewsFromDatabase()
                navEntry.savedStateHandle.set("refreshRequired", false)
            }
        }

        navEntry?.savedStateHandle?.get<String>("selectedCategory")?.let {
            selectedCategory = it
        }
        navEntry?.savedStateHandle?.get<Pair<String, String>>("selectedRange")?.let {
            selectedRange = it
        }
        navEntry?.savedStateHandle?.get<List<String>>("unwantedWords")?.let {
            unwantedWords = it
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val existingNews = savedNewsDAO.getAllNews()
            if (existingNews.isEmpty()) {
                withContext(Dispatchers.Main) {
                    fetchCategoryNews(selectedCategory)
                }
            } else {
                val items = existingNews.map {
                    val tags = savedNewsDAO.getTags(it.id)
                    it.toNewsItem(tags)
                }
                withContext(Dispatchers.Main) {
                    allNews = items
                }
            }
        }
    }

    val filteredNews = allNews
        .filter { newsItem ->
            selectedCategory == "All" || newsItem.category == mapToApiCategory(selectedCategory)
        }
        .filter { newsItem ->
            selectedRange?.let { (startString, endString) ->
                val startDate = LocalDate.parse(startString, outputFormatter)
                val endDate = LocalDate.parse(endString, outputFormatter)
                val newsDate = LocalDate.parse(newsItem.publishedDate, outputFormatter)
                newsDate in startDate..endDate
            } ?: true
        }
        .filter { newsItem ->
            unwantedWords.none { word ->
                newsItem.title.contains(word, ignoreCase = true) ||
                        newsItem.snippet.contains(word, ignoreCase = true)
            }
        }

    val sortedNews = when (selectedSort) {
        SortOption.DATE_ASC -> {
            val featured = filteredNews.filter { it.isFeatured }.sortedBy { it.publishedDate }
            val regular = filteredNews.filter { !it.isFeatured }.sortedBy { it.publishedDate }
            featured + regular
        }
        SortOption.DATE_DESC -> {
            val featured = filteredNews.filter { it.isFeatured }.sortedByDescending { it.publishedDate }
            val regular = filteredNews.filter { !it.isFeatured }.sortedByDescending { it.publishedDate }
            featured + regular
        }
        null -> {
            val featured = filteredNews.filter { it.isFeatured }
            val regular = filteredNews.filter { !it.isFeatured }
            featured + regular
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        FilterSection(
            selectedCategory = selectedCategory,
            onCategorySelected = { category ->
                selectedCategory = category
                navEntry?.savedStateHandle?.apply {
                    remove<String>("selectedCategory")
                    set("selectedCategory", category)
                }
                fetchCategoryNews(category)
            },
            navController = navController
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        NewsList(
            newsItems = sortedNews,
            navController = navController,
            category = selectedCategory
        )
    }
}

enum class SortOption {
    DATE_ASC,
    DATE_DESC
}
