package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import etf.ri.rma.newsfeedapp.data.NewsData
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NewsDetailsScreen(newsId: String, navController: NavController) {
    val allNews = NewsData.getAllNews()
    val selectedNews = allNews.find { it.id == newsId }
    if (selectedNews == null) {
        // Prikazivanje poruke ako vesti nisu pronađene
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Vest sa ID-om $newsId nije pronađena.", modifier = Modifier.testTag("details_not_found"))
        }
        return
    }

    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val selectedDate = try {
        formatter.parse(selectedNews.publishedDate)
    } catch (e: Exception) {
        null
    }

    val relatedNews = if (selectedDate != null) {
        allNews
            .filter { it.category == selectedNews.category && it.id != selectedNews.id }
            .sortedWith(compareBy({ kotlin.math.abs(formatter.parse(it.publishedDate).time - selectedDate.time) }, { it.title }))
            .take(2)
    } else emptyList()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(selectedNews.title, modifier = Modifier.testTag("details_title"))
        Text(selectedNews.snippet, modifier = Modifier.testTag("details_snippet"))
        Text("Kategorija: ${selectedNews.category}", modifier = Modifier.testTag("details_category"))
        Text("Izvor: ${selectedNews.source}", modifier = Modifier.testTag("details_source"))
        Text("Datum: ${selectedNews.publishedDate}", modifier = Modifier.testTag("details_date"))

        Spacer(modifier = Modifier.height(16.dp))
        Text("Povezane vijesti iz iste kategorije")

        relatedNews.forEachIndexed { index, news ->
            Text(
                news.title,
                modifier = Modifier
                    .testTag("related_news_title_${index + 1}")
                    .clickable { navController.navigate("details/${news.id}") }
                    .padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack("home", false) },
            modifier = Modifier.testTag("details_close_button")
        ) {
            Text("Zatvori detalje")
        }
    }
}
