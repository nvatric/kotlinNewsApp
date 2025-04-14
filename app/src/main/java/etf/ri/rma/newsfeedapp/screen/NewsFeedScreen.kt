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
fun NewsFeedScreen(){
    var selectedCategory by remember { mutableStateOf("Sve")}
    val allNews=NewsData.getAllNews()
    val filteredNews = when (selectedCategory) {
        "Politika" -> allNews.filter { it.category == "Politika" }
        "Sport" -> allNews.filter { it.category == "Sport" }
        "Nauka/tehnologija" -> allNews.filter { it.category == "Nauka/tehnologija" }
        "Svijet" -> allNews.filter { it.category =="Svijet" }
        else -> allNews
    }

    Column(modifier = Modifier.padding(16.dp)) {

        FilterSection(
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it }
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (filteredNews.isEmpty()) {
            MessageCard(poruka= "Nema pronađenih vijesti u kategoriji $selectedCategory")
        } else {

            LazyColumn(
                modifier = Modifier.testTag("news_list"),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredNews, key = { it.id }) { newsItem ->
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


