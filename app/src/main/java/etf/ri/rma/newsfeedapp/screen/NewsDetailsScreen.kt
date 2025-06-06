package etf.ri.rma.newsfeedapp.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.data.network.ImagaDAO
import etf.ri.rma.newsfeedapp.data.network.NewsDAO
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.FlowRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailsScreen(
    newsId: String,
    navController: NavController,
    newsDAO: NewsDAO
) {
    val imagaDAO = remember { ImagaDAO() }

    var newsItem by remember { mutableStateOf<NewsItem?>(null) }
   // var imageTags by remember { mutableStateOf<List<String>>(emptyList()) }
    val imageTags = remember { mutableStateListOf<String>() }

    var similarStories by remember { mutableStateOf<List<NewsItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(newsId) {
        isLoading = true
        try {
            val allStories = newsDAO.getAllStories()
            newsItem = allStories.find { it.uuid == newsId }

            newsItem?.let { news ->
                if (news.imageTags != null && news.imageTags!!.isNotEmpty()) {
                    imageTags.clear()
                    imageTags.addAll(news.imageTags!!)
                } else if (!news.imageUrl.isNullOrEmpty()) {
                    try {
                        val tags = imagaDAO.getTags(news.imageUrl!!)
                        imageTags.clear()
                        imageTags.addAll(tags)
                        news.imageTags = ArrayList(tags)
                        allStories.find { it.uuid == news.uuid }?.imageTags = ArrayList(tags)
                    } catch (e: Exception) {
                        Log.e("NewsDetailsScreen", "Greška pri dohvatu tagova: ${e.message}")
                    }
                }

                similarStories = newsDAO.getSimilarStories(newsId)
            }

        } catch (e: Exception) {
            Log.e("NewsDetailsScreen", "Greska: ${e.message}")
        } finally {
            isLoading = false
        }
    }


    Scaffold { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (newsItem != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        Text(newsItem!!.title, style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(8.dp))

                        AsyncImage(
                            model = newsItem!!.imageUrl,
                            contentDescription = "Slika vijesti",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = newsItem!!.category)
                        Spacer(modifier = Modifier.height(16.dp))

                       // if (imageTags.isNotEmpty()) {
                            Text(
                                text = "Tagovi slike:",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            FlowRow(modifier = Modifier.fillMaxWidth()) {
                                imageTags.forEach { tag ->
                                    Text(
                                        text = tag,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                      //  }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Slične vijesti:", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))

                        if (similarStories.isNotEmpty()) {
                            similarStories.take(3).forEach { story ->
                                Text(
                                    text = "• ${story.title}",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable {
                                            navController.navigate("details/${story.uuid}") {
                                                popUpTo("details/${newsId}") { inclusive = true }
                                            }
                                        }
                                )
                            }
                        } else {
                            Text("Nema sličnih vijesti.")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Text("Zatvori detalje")
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Vijest nije pronađena")
            }
        }
    }
}
