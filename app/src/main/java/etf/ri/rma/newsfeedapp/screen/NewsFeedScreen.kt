package etf.ri.rma.newsfeedapp.screen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import etf.ri.rma.newsfeedapp.data.NewsData
import androidx.compose.ui.platform.testTag


@Composable
fun NewsFeedScreen() {
    var selectedCategories by remember { mutableStateOf(setOf("Sve")) }
    var selectedSortOption by remember { mutableStateOf<String?>(null) }

    val allNews = NewsData.getAllNews()

    val filteredNews = if (selectedCategories.contains("Sve")) {
        allNews
    } else {
        allNews.filter { it.category in selectedCategories }
    }

    val sortedNews = when (selectedSortOption) {
        "Datum ⇩" -> filteredNews.sortedBy { it.publishedDate }
        "Datum ⇧" -> filteredNews.sortedByDescending { it.publishedDate }
        else -> filteredNews
    }

    Column(modifier = Modifier.padding(16.dp)) {
        FilterSection(
            selectedCategories = selectedCategories,
            onCategorySelected = { category ->
                selectedCategories = if (category == "Sve") {
                    setOf("Sve")
                } else {
                    if (selectedCategories.contains(category)) {
                        val newSelection = selectedCategories - category
                        if (newSelection.isEmpty()) setOf("Sve") else newSelection
                    } else {
                        selectedCategories - "Sve" + category
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SortSel(
            selectedSortOption = selectedSortOption,
            onSortOptionSelected = { sortOption ->
                selectedSortOption = sortOption
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (sortedNews.isEmpty()) {
            MessageCard(poruka = "Nema pronađenih vijesti za odabrane filtere")
        } else {
            LazyColumn(
                modifier = Modifier.testTag("news_list"),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sortedNews, key = { it.id }) { newsItem ->
                    if (newsItem.isFeatured) {
                        FeaturedNewsCard(newsItem)
                    } else {
                        StandardNewsCard(newsItem)
                    }
                }
            }
        }
    }
}
