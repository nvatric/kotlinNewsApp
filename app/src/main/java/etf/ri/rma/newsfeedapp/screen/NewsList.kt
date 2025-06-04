package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import etf.ri.rma.newsfeedapp.model.NewsItem

@Composable
fun NewsList(newsItems: List<NewsItem>, navController: NavController , category: String) {
    if (newsItems.isEmpty()) {
        MessageCard(poruka = "Nema pronađenih vijesti!")
    } else {
        LazyColumn(
            modifier = Modifier.testTag("news_list")
        ) {
            items(newsItems) { item ->
                if (item.isFeatured) {
                    FeaturedNewsCard(
                        newsItem = item,
                        navController = navController
                    )
                } else {
                    StandardNewsCard(
                        newsItem = item,
                        navController = navController,
                        category
                    )
                }
            }
        }
    }
}
