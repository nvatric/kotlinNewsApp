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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.data.network.ImagaDAO
import etf.ri.rma.newsfeedapp.data.network.NewsDAO
import etf.ri.rma.newsfeedapp.data.SavedNewsDAO
import androidx.compose.foundation.layout.FlowRow
import etf.ri.rma.newsfeedapp.model.TagEntity
import etf.ri.rma.newsfeedapp.model.toNewsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailsScreen(
    newsId: String,
    navController: NavController,
    newsDAO: NewsDAO,
    savedNewsDAO: SavedNewsDAO
) {
    val imagaDAO = remember { ImagaDAO() }

    var newsItem by remember { mutableStateOf<NewsItem?>(null) }
    val imageTags = remember { mutableStateListOf<TagEntity>() }
    var similarStories by remember { mutableStateOf<List<NewsItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    suspend fun loadNewsItem(uuid: String): NewsItem? {
        val entity = savedNewsDAO.getAllNews().find { it.uuid == uuid }
        return entity?.let {
            val tags = savedNewsDAO.getTags(it.id)
            it.copy(tags = tags.map { value -> TagEntity(value = value) }).toNewsItem()
        }
    }

    LaunchedEffect(newsId) {
        isLoading = true
        try {
            newsItem = withContext(Dispatchers.IO) {
                loadNewsItem(newsId)
            }

            newsItem?.let { news ->
                val (newsRoomId, tagsFromDb) = withContext(Dispatchers.IO) {
                    val id = savedNewsDAO.getNewsIdByUUID(news.uuid)
                    val tags = id?.let { savedNewsDAO.getTags(it) } ?: emptyList()
                    Pair(id, tags)
                }

                if (tagsFromDb.isNotEmpty()) {
                    val tagEntities = tagsFromDb.map { TagEntity(value = it) }
                    imageTags.clear()
                    imageTags.addAll(tagEntities)
                    news.imageTags = ArrayList(tagEntities)
                } else if (!news.imageUrl.isNullOrEmpty()) {
                    try {
                        val tags = imagaDAO.getTags(news.imageUrl!!)
                        val tagEntities = tags.map { TagEntity(value = it) }
                        imageTags.clear()
                        imageTags.addAll(tagEntities)
                        news.imageTags = ArrayList(tagEntities)

                        if (newsRoomId != null) {
                            withContext(Dispatchers.IO) {
                                savedNewsDAO.addTags(tags, newsRoomId)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("NewsDetailsScreen", "Greška pri dohvatu tagova: ${e.message}")
                    }
                }

                similarStories = newsDAO.getSimilarStories(newsId)
            }

        } catch (e: Exception) {
            Log.e("NewsDetailsScreen", "Greška: ${e.message}")
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
                LazyColumn(modifier = Modifier.weight(1f)) {
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

                        Text(
                            text = "Tagovi slike:",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        FlowRow(modifier = Modifier.fillMaxWidth()) {
                            imageTags.forEach { tag ->
                                Text(
                                    text = tag.value,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }

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
