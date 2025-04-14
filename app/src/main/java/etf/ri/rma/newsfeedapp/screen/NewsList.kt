package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.runtime.Composable
import etf.ri.rma.newsfeedapp.model.NewsItem


@Composable
fun NewsList(newsItem: List<NewsItem>) {
    if (newsItem.isEmpty()) {
        MessageCard(poruka = "Nema pronađenih vijesti!")
    } else {
        LazyColumn(
            modifier = Modifier.testTag("news_list")
        ) {
            items(newsItem.size) { index ->
                val item = newsItem[index]
                if (item.isFeatured) {
                    FeaturedNewsCard(newsItem = item)
                } else {
                    StandardNewsCard(newsItem = item)
                }
            }
        }
    }
}
